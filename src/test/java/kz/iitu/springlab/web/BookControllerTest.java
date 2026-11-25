package kz.iitu.springlab.web;

import kz.iitu.springlab.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetBooks() throws Exception {
        when(bookService.getBooks())
                .thenReturn(List.of("Clean Code", "Effective Java"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddBook() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .param("title", "New Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Book added successfully"));

        verify(bookService).addBook("New Book");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Book deleted successfully"));

        verify(bookService).deleteBook(0);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowUserToReachController() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .param("title", "New Book"))
                .andExpect(status().isOk());

        verify(bookService).addBook("New Book");
    }
}
