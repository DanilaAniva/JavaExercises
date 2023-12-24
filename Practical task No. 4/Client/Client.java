import com.example.rsocket.domain.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.rsocket.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.Payload;
import org.springframework.messaging.rsocket.support.RSocketStrategies;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class RSocketClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RSocketClientApplication.class, args);
    }

    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new CustomEncoder()))
                .build();
    }
}
