package kz.iitu.springlab.integration;

import kz.iitu.springlab.entity.Book;
import kz.iitu.springlab.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class BookPostgresIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("library")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name",
                postgres::getDriverClassName);
    }

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSaveAndFindBookInPostgres() {
        Book book = new Book("PostgreSQL Integration", "Test Author");

        Book savedBook = bookRepository.save(book);

        Book foundBook = bookRepository.findById(savedBook.getId())
                .orElseThrow();

        assertThat(foundBook.getTitle())
                .isEqualTo("PostgreSQL Integration");
        assertThat(foundBook.getAuthor())
                .isEqualTo("Test Author");
    }
}
