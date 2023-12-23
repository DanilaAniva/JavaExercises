import io.reactivex.rxjava3.core.Observable;
import java.util.Random;

public class Main {
    private static UserFriend[] userFriends; // Массив объектов UserFriend

    public static void main(String[] args) {
        // Инициализация массива случайными данными
        Random random = new Random();
        userFriends = new UserFriend[10];
        for (int i = 0; i < userFriends.length; i++) {
            userFriends[i] = new UserFriend(random.nextInt(10), random.nextInt(10));
        }

        // Пример использования
        int[] userIds = {1, 2, 3, 4, 5}; // массив случайных userId
        Observable.fromArray(userIds)
                  .flatMap(Main::getFriends)
                  .subscribe(System.out::println);
    }

    public static Observable<UserFriend> getFriends(int userId) {
        return Observable.fromArray(userFriends)
                         .filter(userFriend -> userFriend.getUserId() == userId);
    }
}
