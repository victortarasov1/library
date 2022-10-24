package com.example.library.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.library.exception.AuthorContainsBookException;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.dto.AuthorDto;
import com.example.library.dto.BookDto;

@RestController
@RequestMapping("/library")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class LibraryRestController {
    private ModelMapper modelMapper;
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private BookService bookService;
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDto>> findAll() {
        var authors = authorRepository.findAllByActuality(Actuality.ACTIVE);
        return ResponseEntity.ok(authors.stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList());
    }


    @GetMapping("/authors/{id}")
    public ResponseEntity<List<BookDto>> findById(@PathVariable Long id) {
        var author = authorRepository.findAuthorByIdAndActuality(id, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(id));
        return ResponseEntity.ok(author.getBooks().stream().map(book -> modelMapper.map(book, BookDto.class)).toList());
    }

    @PatchMapping("/authors")
    public ResponseEntity<AuthorDto> changeAuthor(@RequestBody @Valid AuthorDto dto) {
        var author = authorRepository.findAuthorByIdAndActuality(dto.getId(), Actuality.ACTIVE)
                .orElseThrow(() -> new AuthorNotFoundException(dto.getId()));
        author.setAge(dto.getAge());
        author.setSecondName(dto.getSecondName());
        author.setName(dto.getName());
        var updated = authorRepository.save(author);
        return ResponseEntity.ok(modelMapper.map(updated, AuthorDto.class));

    }

    @PostMapping("/authors")
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody @Valid AuthorDto dto) {
        var author = dto.toAuthor();
        return ResponseEntity.ok(modelMapper.map(authorRepository.save(author), AuthorDto.class));
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable Long id) {
        var author = authorRepository.findAuthorByIdAndActuality(id, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(id));
        author.setActuality(Actuality.REMOVED);
        author.setBooks(null);
        authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }

    @PostMapping("/authors/{id}")
    public ResponseEntity<BookDto> addBook(@PathVariable Long id, @RequestBody @Valid BookDto dto) throws AuthorContainsBookException {
        var author = authorRepository.findAuthorByIdAndActuality(id, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(id));
        var book = dto.toBook();
        if(author.getBooks() != null && author.getBooks().contains(book)){
            throw new AuthorContainsBookException();
        }
        bookService.checkIfAnotherAuthorsHaveThisBook(book);
        author.addBook(book);
        var books = authorRepository.save(author).getBooks();
        return ResponseEntity.ok(modelMapper.map(books.get(books.size() - 1), BookDto.class));
    }

    @PatchMapping("/authors/{id}/books")
    public ResponseEntity<BookDto> changeBook(@PathVariable Long id, @RequestBody @Valid BookDto dto) {
        var author = authorRepository.findAuthorByIdAndActuality(id, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(id));
        author.setBooks(author.getBooks().stream().filter(book -> !book.getId().equals(dto.getId())).toList());
        var book = dto.toBook();
        if(author.getBooks().contains(book)){
            throw new AuthorContainsBookException();
        }
        bookService.checkIfAnotherAuthorsHaveThisBook(book);
        author.addBook(book);
        var books = authorRepository.save(author).getBooks();
        return ResponseEntity.ok(modelMapper.map(books.get(books.size() - 1), BookDto.class));
    }

    @DeleteMapping("/authors/{authorsId}/books/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long authorsId, @PathVariable Long bookId) {
        var author = authorRepository.findAuthorByIdAndActuality(authorsId, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(authorsId));
        var book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        author.removeBook(book);
        authorRepository.save(author);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> getBooks() {
        var books = bookRepository.findAll();
        books = books.stream().filter(book -> book.getAuthors().size() != 0).toList();
        return ResponseEntity.ok(books.stream().map(book -> modelMapper.map(book, BookDto.class)).toList());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<List<AuthorDto>> getAuthors(@PathVariable Long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return ResponseEntity.ok(book.getAuthors().stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList());
    }
    
}
