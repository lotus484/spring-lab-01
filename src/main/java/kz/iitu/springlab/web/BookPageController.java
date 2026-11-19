package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookPageController {

    private final BookService bookService;

    public BookPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll(null));
        return "books";
    }

    @GetMapping("/books/new")
    public String newBookForm() {
        return "book-form";
    }

    @PostMapping("/books")
    public String createBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam int year,
            RedirectAttributes redirectAttributes) {

        Book book = new Book(null, title, author, year);
        bookService.create(book);

        redirectAttributes.addFlashAttribute(
                "message", "Book added successfully!");

        return "redirect:/books";
    }
}