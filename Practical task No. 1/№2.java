import java.util.Scanner;
import java.lang.Thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Основной класс программы
public class Main {
    // Точка входа в программу
    public static void main(String[] args) throws Exception {
        // Создание пула потоков с 2 потоками
        ExecutorService es = Executors.newFixedThreadPool(2);

        // Создание сканера для чтения из стандартного ввода
        Scanner scan = new Scanner(System.in);

        // Чтение первого ввода пользователя и запуск задачи
        int input1 = Integer.parseInt(scan.nextLine());
        Future<?> ftr = es.submit(new MyRunnable(input1));

        // Бесконечный цикл для чтения последующих вводов пользователя
        while (true) {
            String input2 = scan.nextLine();
            if (!ftr.isDone()) {
                // Если предыдущая задача не завершена, запускается новая задача
                es.submit(new MyRunnable(Integer.parseInt(input2)));
            }
             break; // Прерывает цикл после первой итерации (нужно исправить для непрерывной работы)
        }

        // Завершение работы пула потоков
        es.shutdown();
    }
}

// Класс, реализующий интерфейс Runnable для выполнения в потоке
class MyRunnable implements Runnable {
    // Хранит число для обработки
    Integer number;

    // Конструктор, инициализирующий объект класса с числом
    public MyRunnable(int number) {
        this.number = number;
    }

    // Метод, выполняемый потоком
    @Override
    public void run() {
        try {
            // Получение имени текущего потока для логирования
            String threadName = Thread.currentThread().getName();

            // Вывод сообщения о начале выполнения задачи
            System.out.println("["+threadName+"] Waiting for response... [Math.pow("+number+",2)]");

            // Генерация случайной задержки от 1 до 5 секунд
            long random = (long)(Math.random() * 4000) + 1000;
            double result = Math.pow(number, 2);

            // Имитация задержки выполнения
            Thread.sleep(random);

            // Вывод результата после задержки
            System.out.println(
                "["+threadName+"]" +
                " Square of the number " + number + " is: " + result +
                " [response time: " + random + "ms]"
            );
        } catch (Exception ex) {
            // Обработка исключений
            ex.printStackTrace(System.out);
        }
    }
}