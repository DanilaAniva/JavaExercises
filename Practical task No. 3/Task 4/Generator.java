import io.reactivex.rxjava3.core.Observable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FileGenerator {
    public Observable<File> generateFiles() {
        Random random = new Random();
        return Observable.interval(100 + random.nextInt(901), TimeUnit.MILLISECONDS)
                         .map(tick -> new File(
                             File.FileType.values()[random.nextInt(File.FileType.values().length)],
                             10 + random.nextInt(91)
                         ));
    }
}
