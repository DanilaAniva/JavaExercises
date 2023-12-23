import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class TemperatureSensor {
    public Observable<Integer> temperatureStream() {
        Random random = new Random();
        return Observable.interval(1, TimeUnit.SECONDS)
                         .map(tick -> 15 + random.nextInt(16));
    }
}

public class CO2Sensor {
    public Observable<Integer> co2Stream() {
        Random random = new Random();
        return Observable.interval(1, TimeUnit.SECONDS)
                         .map(tick -> 30 + random.nextInt(71));
    }
}

public class AlarmSystem {
    private final int TEMP_THRESHOLD = 25;
    private final int CO2_THRESHOLD = 70;
    private boolean tempWarning = false;
    private boolean co2Warning = false;

    public AlarmSystem(TemperatureSensor tempSensor, CO2Sensor co2Sensor) {
        Observable<Integer> tempStream = tempSensor.temperatureStream();
        Observable<Integer> co2Stream = co2Sensor.co2Stream();

        Disposable tempSubscription = tempStream.subscribe(this::handleTemperature);
        Disposable co2Subscription = co2Stream.subscribe(this::handleCO2);
    }

    private void handleTemperature(int temperature) {
        if (temperature > TEMP_THRESHOLD) {
            System.out.println("Temperature warning: " + temperature);
            tempWarning = true;
        } else {
            tempWarning = false;
        }
        checkForAlarm();
    }

    private void handleCO2(int co2) {
        if (co2 > CO2_THRESHOLD) {
            System.out.println("CO2 warning: " + co2);
            co2Warning = true;
        } else {
            co2Warning = false;
        }
        checkForAlarm();
    }

    private void checkForAlarm() {
        if (tempWarning && co2Warning) {
            System.out.println("ALARM!!!");
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TemperatureSensor tempSensor = new TemperatureSensor();
        CO2Sensor co2Sensor = new CO2Sensor();
        AlarmSystem alarm = new AlarmSystem(tempSensor, co2Sensor);

        Thread.sleep(10000); // Запускаем систему на 10 секунд
    }
}
