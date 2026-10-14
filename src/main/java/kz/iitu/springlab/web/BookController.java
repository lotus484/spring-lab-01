package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> findAll(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "10") int limit) {

        return bookService.findAll(author)
                .stream()
                .limit(Math.max(0, limit))
                .toList();
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable long id) {
        return bookService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Book not found"));
    }

    @GetMapping(
            value = "/{id}/citation",
            produces = "text/plain")
    public String citation(@PathVariable long id) {
        Book book = findById(id);

        return book.author() + ". "
                + book.title() + ". "
                + book.year() + ".";
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book created = bookService.create(book);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    @PutMapping("/{id}")
    public Book update(
            @PathVariable long id,
            @RequestBody Book book) {

        return bookService.update(id, book)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Book not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        if (!bookService.delete(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found");
        }
    }
}