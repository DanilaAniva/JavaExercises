import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MaxFinderDemo {

    // Класс для последовательного поиска максимального элемента
    public static class SequentialMaxFinder {
        // Метод для поиска максимального значения в массиве
        public static int findMax(int[] array) throws InterruptedException {
            int max = Integer.MIN_VALUE;
            for (int value : array) {
                max = Math.max(max, value);
                Thread.sleep(1); // Искусственная задержка для имитации обработки
            }
            return max;
        }
    }

    // Класс для многопоточного поиска максимального элемента
    public static class ConcurrentMaxFinder {
        // Метод для поиска максимального значения в массиве с использованием многопоточности
        public static int findMax(int[] array) throws Exception {
            // Получение количества доступных процессоров для определения числа потоков
            int threads = Runtime.getRuntime().availableProcessors();
            ExecutorService service = Executors.newFixedThreadPool(threads);
            Future<Integer>[] futures = new Future[threads];

            // Разбиение массива на части для параллельной обработки
            int chunkSize = array.length / threads;
            for (int i = 0; i < threads; i++) {
                final int start = i * chunkSize;
                final int end = (i + 1) * chunkSize;
                futures[i] = service.submit(() -> {
                    int max = Integer.MIN_VALUE;
                    for (int j = start; j < end; j++) {
                        max = Math.max(max, array[j]);
                        Thread.sleep(1); // Искусственная задержка
                    }
                    return max;
                });
            }

            // Сбор результатов из всех потоков
            int max = Integer.MIN_VALUE;
            for (Future<Integer> future : futures) {
                max = Math.max(max, future.get());
            }
            service.shutdown();
            return max;
        }
    }

    // Класс для поиска максимального элемента с использованием ForkJoin
    public static class ForkJoinMaxFinder extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start, end;

        // Конструктор класса
        public ForkJoinMaxFinder(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        // Рекурсивный метод для поиска максимального значения
        @Override
        protected Integer compute() {
            // Если размер сегмента достаточно мал, выполняем прямой поиск
            if (end - start <= 1000) {
                int max = Integer.MIN_VALUE;
                for (int i = start; i < end; i++) {
                    max = Math.max(max, array[i]);
                    Thread.sleep(1); // Искусственная задержка
                }
                return max;
            } else {
                // Деление задачи на две подзадачи
                int mid = (start + end) / 2;
                ForkJoinMaxFinder left = new ForkJoinMaxFinder(array, start, mid);
                left.fork();
                ForkJoinMaxFinder right = new ForkJoinMaxFinder(array, mid, end);
                // Соединение результатов подзадач
                return Math.max(right.compute(), left.join());
            }
        }

        // Статический метод для запуска поиска с использованием ForkJoin
        public static int findMax(int[] array) {
            ForkJoinPool pool = new ForkJoinPool();
            return pool.invoke(new ForkJoinMaxFinder(array, 0, array.length));
        }
    }

    // Тестовый метод для проверки всех трех подходов
    public static void main(String[] args) {
        // Создание и инициализация тестового массива
        int[] array = new int[10000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000); // Заполнение случайными числами
        }

        // Проверка каждого метода и измерение времени выполнения
        try {
            // Последовательный поиск
            long startTime = System.currentTimeMillis();
            int maxSequential = SequentialMaxFinder.findMax(array);
            long endTime = System.currentTimeMillis();
            System.out.println("Sequential: Max = " + maxSequential + ", Time = " + (endTime - startTime) + " ms");

            // Многопоточный поиск
            startTime = System.currentTimeMillis();
            int maxConcurrent = ConcurrentMaxFinder.findMax(array);
            endTime = System.currentTimeMillis();
            System.out.println("Concurrent: Max = " + maxConcurrent + ", Time = " + (endTime - startTime) + " ms");

            // Поиск с использованием ForkJoin
            startTime = System.currentTimeMillis();
            int maxForkJoin = ForkJoinMaxFinder.findMax(array);
            endTime = System.currentTimeMillis();
            System.out.println("ForkJoin: Max = " + maxForkJoin + ", Time = " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}