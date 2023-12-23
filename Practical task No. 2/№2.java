import java.io.*; // Импорт классов для ввода/вывода.
import java.nio.channels.FileChannel; // Импорт для работы с каналами файлов.
import java.nio.file.Files; // Импорт для работы с файловой системой.
import java.nio.file.Path; // Импорт для работы с путями в файловой системе.
import java.nio.file.Paths; // Импорт для работы с объектами Path.
import org.apache.commons.io.FileUtils; // Импорт Apache Commons IO для работы с файлами.

import static edu.mirea.rksp.pr2.task1.Main.createFile; // Статический импорт метода createFile.

public class Main {
    // Метод для копирования файла с использованием потоков ввода-вывода.
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source); // Открытие потока ввода из исходного файла.
            os = new FileOutputStream(dest); // Открытие потока вывода в целевой файл.
            byte[] buffer = new byte[65536]; // Буфер для чтения данных.
            int length;
            while ((length = is.read(buffer)) > 0) { // Чтение и запись данных пока есть что читать.
                os.write(buffer, 0, length);
            }
        } finally {
            is.close(); // Закрытие потока ввода.
            os.close(); // Закрытие потока вывода.
        }
    }

    // Метод для копирования файла с использованием каналов (FileChannel).
    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel(); // Открытие канала исходного файла.
            destChannel = new FileOutputStream(dest).getChannel(); // Открытие канала целевого файла.
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size()); // Перенос данных из источника в цель.
        } finally {
            sourceChannel.close(); // Закрытие исходного канала.
            destChannel.close(); // Закрытие целевого канала.
        }
    }

    // Метод для копирования файла с использованием Apache Commons IO.
    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest); // Копирование файла.
    }

    // Метод для копирования файла с использованием класса Files из Java 7.
    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath()); // Копирование файла.
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String currentDir = System.getProperty("user.dir"); // Получение текущего рабочего каталога.
        new File(currentDir+"/tmp").mkdir(); // Создание временного каталога.
        Path tmpDir = Paths.get(currentDir, "tmp"); // Создание объекта Path для временного каталога.
        System.out.println("Working directory is - "+tmpDir);

        // Удаление предыдущих файлов в директории.
        File directory = new File(tmpDir.toString());
        FileUtils.cleanDirectory(directory);

        // Создание файла заданного размера.
        final int FILE_SIZE = 100; // Размер файла в мегабайтах.
        File myFile = createFile("FILE", tmpDir.toString(), FILE_SIZE * 1024 * 1024);

        // Копирование файла с использованием различных методов и замер времени.
        File dest;
        long start;
        // Копирование с использованием IO Streams, FileChannel, Apache Commons IO и класса Files.
        // Для каждого метода выводится затраченное время.
    }
}
