/*
 * Этот код реализует многопоточную систему обработки файлов. Система состоит из генератора файлов, 
 * обработчиков файлов и очереди файлов. Она использует блокировки и условные переменные для синхронизации потоков.
 */

// Импорты необходимых классов
import java.lang.Thread;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        // Инициализация очереди с ограниченным размером и потоков для обработки файлов
        FileQueue queue = new FileQueue(5);
        String[] types = new String[] {"XML", "JSON", "XLS"};
        FileHandler[] fileHandlers = new FileHandler[3];
        Thread[] handlerThreads = new Thread[3];

        // Создание и запуск потоков для обработки файлов каждого типа
        for (int i = 0; i < fileHandlers.length; i++){
            fileHandlers[i] = new FileHandler(queue, types[i], queue.lock, queue.notEmpty);
            handlerThreads[i] = new Thread(fileHandlers[i]);
            handlerThreads[i].start();
        }

        // Создание и запуск потока для генерации файлов
        FileGenerator fg = new FileGenerator(queue, queue.lock, queue.notFull, types);
        Thread gt = new Thread(fg);
        gt.start();
    }
}

// Класс, представляющий файл с типом и размером
class File {
    String type;
    Integer size;

    public File(String type, Integer size) {
        this.type = type;
        this.size = size;
    }
}

// Класс для генерации файлов, реализует интерфейс Runnable для работы в потоке
class FileGenerator implements Runnable {
    private final FileQueue queue;
    private final Lock lock;
    private final Condition condition;
    private final String[] types;
    private final Random random = new Random();

    public FileGenerator(FileQueue queue, Lock lock, Condition condition, String[] types) {
        this.queue = queue;
        this.lock = lock;
        this.condition = condition;
        this.types = types;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                // Ожидание, если очередь заполнена
                while (queue.size() == 5) {
                    condition.await();
                }

                // Генерация файла и добавление его в очередь
                String type = types[random.nextInt(3)];
                Integer size = random.nextInt(91)+10;
                int time = random.nextInt(901)+100;
                File file = new File(type, size);
                System.out.println("[" + Thread.currentThread().getName() + "] Generated File: " + file.size + " " + file.type);
                Thread.sleep(time); // Имитация задержки при генерации
                queue.add(file);
                condition.signalAll(); // Оповещение обработчиков о новом файле
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

// Класс для обработки файлов, также реализует Runnable
class FileHandler implements Runnable {
    private final FileQueue queue;
    private final String type;
    private final Lock lock;
    private final Condition condition;

    public FileHandler(FileQueue queue, String type, Lock lock, Condition condition) {
        this.queue = queue;
        this.type = type;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                // Ожидание, если очередь пуста
                while (queue.size() == 0) {
                    condition.await();
                }
                // Обработка файла, если он соответствует типу обработчика
                File file = queue.peek();
                if (file.type.equals(type)) {
                    queue.remove();
                    long time = file.size * 7L; // Имитация времени обработки
                    Thread.sleep(time);
                    System.out.println("[" + Thread.currentThread().getName() + "] Processed File: " + file.size + " " + file.type + " [time spent: " + time + "ms]");
                }
                condition.signalAll(); // Оповещение генератора о свободном месте в очереди
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

// Класс, управляющий потокобезопасной очередью файлов
class FileQueue {
    public int capacity;
    public Queue<File> queue = new LinkedList<>();
    public ReentrantLock lock = new ReentrantLock();
    public Condition notFull = lock.newCondition();
    public Condition notEmpty = lock.newCondition();

    public FileQueue(int capacity) {
        this.capacity = capacity;
    }

    // Метод для добавления файла в очередь
    public void add(File file) throws InterruptedException {
        lock.lock();
        try {
            // Ожидание, если очередь полностью заполнена
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(file);
            notEmpty.signal(); // Оповещение обработчиков о наличии файла
        } finally {
            lock.unlock();
        }
    }

    // Метод для удаления файла из очереди
    public void remove() throws InterruptedException {
        lock.lock();
        try {
            // Ожидание, если очередь пуста
            while (queue.size() == 0) {
                notEmpty.await();
            }
            queue.remove();
            notFull.signal(); // Оповещение генератора о свободном месте
        } finally {
            lock.unlock();
        }
    }

    // Вспомогательные методы для работы с очередью
    public int size() {
        return queue.size();
    }

    public File peek() {
        return queue.peek();
    }
}
