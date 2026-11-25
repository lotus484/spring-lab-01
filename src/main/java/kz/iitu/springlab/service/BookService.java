package kz.iitu.springlab.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final List<String> books = new ArrayList<>(
            List.of("Clean Code", "Effective Java", "Spring in Action")
    );

    public List<String> getBooks() {
        return new ArrayList<>(books);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(int index) {
        if (index >= 0 && index < books.size()) {
            books.remove(index);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void addBook(String title) {
        if (title != null && !title.isBlank()) {
            books.add(title);
        }
    }
}