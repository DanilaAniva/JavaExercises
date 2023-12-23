public class File {
    public enum FileType {
        XML, JSON, XLS
    }

    private FileType fileType;
    private int fileSize; // размер файла от 10 до 100

    public File(FileType fileType, int fileSize) {
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public FileType getFileType() {
        return fileType;
    }

    public int getFileSize() {
        return fileSize;
    }
}
