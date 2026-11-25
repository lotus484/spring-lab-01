package kz.iitu.springlab.repository;

import kz.iitu.springlab.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSaveAndFindBook() {
        Book book = new Book("Clean Code", "Robert Martin");

        Book savedBook = bookRepository.save(book);

        Optional<Book> foundBook =
                bookRepository.findById(savedBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Clean Code");
        assertThat(foundBook.get().getAuthor()).isEqualTo("Robert Martin");
    }

    @Test
    void shouldDeleteBook() {
        Book book = bookRepository.save(
                new Book("Effective Java", "Joshua Bloch")
        );

        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }

    @Test
    void shouldCountBooks() {
        bookRepository.save(new Book("Book One", "Author One"));
        bookRepository.save(new Book("Book Two", "Author Two"));

        assertThat(bookRepository.count()).isEqualTo(2);
    }
}
