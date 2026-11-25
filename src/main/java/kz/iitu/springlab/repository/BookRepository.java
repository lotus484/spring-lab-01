package kz.iitu.springlab.repository;

import kz.iitu.springlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
