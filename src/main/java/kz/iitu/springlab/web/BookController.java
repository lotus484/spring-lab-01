package kz.iitu.springlab.web;

import kz.iitu.springlab.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<String> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping
    public Map<String, String> addBook(@RequestParam String title) {
        bookService.addBook(title);
        return Map.of("message", "Book added successfully");
    }

    @DeleteMapping("/{index}")
    public Map<String, String> deleteBook(@PathVariable int index) {
        bookService.deleteBook(index);
        return Map.of("message", "Book deleted successfully");
    }
}