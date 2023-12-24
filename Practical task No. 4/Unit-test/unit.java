import com.example.rsocket.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Test
    public void shouldReturnBooks() {
        // Given
        List<Book> books = Arrays.asList(
            new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams"),
            new Book("The Lord of the Rings", "J.R.R. Tolkien"),
            new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling")
        );
        BookRepository bookRepository = new InMemoryBookRepository(books);
        BookController bookController = new BookController(bookRepository);

        // When
        Flux<Book> booksFlux = bookController.findBooks("galaxy");

        // Then
        StepVerifier.create(booksFlux)
            .expectNext(books.get(0))
            .expectComplete()
            .verify();
    }

    @Test
    public void shouldReturnBook() {
        // Given
        Book book = new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams");
        BookRepository bookRepository = new InMemoryBookRepository(List.of(book));
        BookController bookController = new BookController(bookRepository);

        // When
        Mono<Book> bookMono = bookController.getBook(1L);

        // Then
        assertThat(bookMono.block()).isEqualTo(book);
    }
}
