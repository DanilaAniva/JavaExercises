public class Main {
    public static void main(String[] args) {
        FileGenerator generator = new FileGenerator();
        FileQueue queue = new FileQueue();

        generator.generateFiles()
                 .take(20) // ограничим количество файлов для генерации
                 .subscribe(queue::addFile);

        FileProcessor xmlProcessor = new FileProcessor(File.FileType.XML);
        FileProcessor jsonProcessor = new FileProcessor(File.FileType.JSON);
        FileProcessor xlsProcessor = new FileProcessor(File.FileType.XLS);

        xmlProcessor.processFiles(queue.getFileQueue());
        jsonProcessor.processFiles(queue.getFileQueue());
        xlsProcessor.processFiles(queue.getFileQueue());
    }
}
