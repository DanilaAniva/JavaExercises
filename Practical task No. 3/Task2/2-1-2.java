import io.reactivex.rxjava3.core.Observable;
import java.util.Random;

public class Task212 {
    public static void main(String[] args) {
        Observable<Integer> randomNumbers = Observable.create(emitter -> {
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                emitter.onNext(random.nextInt(1001));
            }
            emitter.onComplete();
        });

        randomNumbers.filter(number -> number > 500)
                     .subscribe(System.out::println);
    }
}
