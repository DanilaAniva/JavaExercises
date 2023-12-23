import io.reactivex.rxjava3.core.Observable;
import java.util.Random;

public class Task232 {
    public static void main(String[] args) {
        Observable<Integer> randomNumbers = Observable.create(emitter -> {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                emitter.onNext(random.nextInt(100));
            }
            emitter.onComplete();
        });

        randomNumbers.take(5)
                     .subscribe(System.out::println);
    }
}
