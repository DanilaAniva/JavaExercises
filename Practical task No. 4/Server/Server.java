import com.example.rsocket.domain.Book;
import com.example.rsocket.repository.BookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.rsocket.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.Payload;
import org.springframework.messaging.rsocket.support.RSocketStrategies;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class RSocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RSocketServerApplication.class, args);
    }

    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new CustomEncoder()))
                .build();
    }

    @Controller
    class BookController {

        @Autowired
        private BookRepository bookRepository;

        /**
         * Возвращает список книг, удовлетворяющих заданному запросу.
         *
         * @param query Запрос
         * @return Список книг
         */
        @MessageMapping("/books")
        public Flux<Book> findBooks(@Payload String query) {
            // Фильтрует книги по заданному запросу
            return bookRepository.findAll().filter(book -> book.getName().contains(query));
        }

        /**
         * Возвращает информацию о книге по ее идентификатору.
         *
         * @param id Идентификатор книги
         * @return Информация о книге
         */
        @MessageMapping("/books/{id}")
        public Mono<Book> getBook(@PathVariable Long id) {
            // Возвращает книгу по ее идентификатору
            return bookRepository.findById(id);
        }

        /**
         * Возвращает поток книг, обновляемых в реальном времени.
         *
         * @return Поток книг
         */
        @MessageMapping("/books/stream")
        public Flux<Book> streamBooks() {
            // Возвращает поток всех книг
            return bookRepository.findAll().subscribeOn(Schedulers.elastic());
        }

        /**
         * Создает подписку на обновления информации о книге по ее идентификатору.
         *
         * @param id Идентификатор книги
         * @return Результат операции
         */
        @MessageMapping("/books/{id}/channel")
        public Mono<Void> subscribeToBookUpdates(@PathVariable Long id) {
            // Возвращает книгу по ее идентификатору
            return bookRepository.findById(id)
                    // Обновляет информацию о книге
                    .flatMap(book -> bookRepository.save(book.withUpdatedAt(Instant.now())))
                    // Закрывает поток
                    .subscribe();
        }

        /**
         * Добавляет книгу в список избранного.
         *
         * @param id Идентификатор книги
         * @return Результат операции
         */
        @MessageMapping("/books/{id}/fire-and-forget")
        public Mono<Void> addBookToFavorites(@PathVariable Long id) {
            // Возвращает книгу по ее идентификатору
            return bookRepository.findById(id)
                    // Добавляет книгу в список избранного
                    .flatMap(book -> bookRepository.save(book.withFavorite(true)))
                    // Не возвращает результата
                    .subscribe();
        }
    }
}
