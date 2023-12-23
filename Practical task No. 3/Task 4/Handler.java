import io.reactivex.rxjava3.core.Observable;

public class FileProcessor {
    private File.FileType fileType;

    public FileProcessor(File.FileType fileType) {
        this.fileType = fileType;
    }

    public void processFiles(Observable<File> fileStream) {
        fileStream.filter(file -> file.getFileType() == this.fileType)
                  .subscribe(file -> {
                      try {
                          Thread.sleep(file.getFileSize() * 7); // время обработки файла
                          System.out.println("Processed " + file.getFileType() + " file of size " + file.getFileSize());
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  });
    }
}
