import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    // метод createFile
    public static File createFile(final String filename, final Path directory, final long sizeInBytes) throws IOException {
        Path filePath = directory.resolve(filename);
        File file = filePath.toFile();
        file.createNewFile();  // Без изменений

        // Используем try-with-resources для автоматического закрытия RandomAccessFile
        try (var raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(sizeInBytes);
        }

        return file;
    }

    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir");
        Path tmpDir = Paths.get(currentDir, "tmp");
        tmpDir.toFile().mkdirs(); // Используем mkdirs(), чтобы создать все несуществующие родительские директории

        System.out.println("Рабочая директория - " + tmpDir);

        // Создания файла и записи в него
        File myFile = createFile("file.txt", tmpDir, 0);
        String str = "Привет, мир!\nНовая, вторая строка\nИ еще одна строка\nЕще одна строка\n...";
        Files.write(myFile.toPath(), str.getBytes()); // Напрямую используем str.getBytes() в Files.write()

        // Чтение файла
        String fileContents = Files.readString(myFile.toPath());
        System.out.println("Содержимое файла " + myFile.getName() + ":\n" + fileContents);
    }
}
