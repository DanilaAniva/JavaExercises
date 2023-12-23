import io.reactivex.rxjava3.core.Observable;
import java.util.Random;

public class Task222 {
    public static void main(String[] args) {
        Observable<Integer> stream1 = Observable.create(emitter -> {
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                emitter.onNext(random.nextInt(10));
            }
            emitter.onComplete();
        });

        Observable<Integer> stream2 = Observable.create(emitter -> {
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                emitter.onNext(random.nextInt(10));
            }
            emitter.onComplete();
        });

        Observable.concat(stream1, stream2)
                  .subscribe(System.out::println);
    }
}
