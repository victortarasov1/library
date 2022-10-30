package com.example.library.service;

import com.example.library.exception.AuthorContainsBookException;
import com.example.library.model.Actuality;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    private AuthorServiceImpl underTest;
    @BeforeEach
    void setUp() {
        underTest = new AuthorServiceImpl(bookRepository, authorRepository);

    }
    @Test
    void addBook_shouldReturnException() {
        var author = new Author(1L, "name", "second name", 33, Actuality.ACTIVE, null );
        var book = new Book(1L, "title", "description", null);
        author.addBook(book);
        var exception = assertThrows(AuthorContainsBookException.class, () -> {
            underTest.addBook(author, book);
        });
        assertEquals(exception.getMessage(), "this author already contains this book");
    }

    @Test
    void addBook_shouldReturnListOfBooks(){
        var author = new Author(1L, "name", "second name", 33, Actuality.ACTIVE, null );
        var book = new Book(1L, "title", "description", null);
        var authorWithBook = new Author(1L, "name", "second name", 33, Actuality.ACTIVE, List.of(book) );
        book.setAuthors(List.of(authorWithBook));
        when(authorRepository.save(author)).thenReturn(authorWithBook);
        when(bookRepository.findAll()).thenReturn(List.of(book));
        var books = underTest.addBook(author, book);
        assertEquals(books, List.of(book));
        assertEquals(books.get(0).getAuthors().get(0), author);
    }

}