import io.reactivex.rxjava3.subjects.PublishSubject;

public class FileQueue {
    private PublishSubject<File> fileQueue = PublishSubject.create();

    public PublishSubject<File> getFileQueue() {
        return fileQueue;
    }

    public void addFile(File file) {
        fileQueue.onNext(file);
    }
}
