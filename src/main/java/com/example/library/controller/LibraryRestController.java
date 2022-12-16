package com.example.library.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.example.library.exception.AuthorContainsBookException;
import com.example.library.exception.AuthorDoesntContainsBookException;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
    @GetMapping("/authors")
    public List<AuthorDto> findAll() {
        return authorRepository.findAllByActuality(Actuality.ACTIVE).stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }


    @GetMapping("/authors/{id}")
    public List<BookDto> findById(@PathVariable Long id) {
        return bookRepository.getBooksByAuthorId(id).stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }

    @PatchMapping("/authors")
    public AuthorDto changeAuthor(@RequestBody @Valid AuthorDto dto) {
        var author = authorRepository.findAuthorByIdAndActuality(dto.getId(), Actuality.ACTIVE)
                .orElseThrow(() -> new AuthorNotFoundException(dto.getId()));
        author.setAge(dto.getAge());
        author.setSecondName(dto.getSecondName());
        author.setName(dto.getName());
        var updated = authorRepository.save(author);
        return modelMapper.map(updated, AuthorDto.class);

    }

    @PostMapping("/authors")
    public AuthorDto addAuthor(@RequestBody @Valid AuthorDto dto) {
        var author = dto.toAuthor();
        return modelMapper.map(authorRepository.save(author), AuthorDto.class);
    }

    @DeleteMapping("/authors/{id}")
    public String deleteAuthorById(@PathVariable Long id) {
        var author = authorRepository.findAuthorByIdAndActuality(id, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(id));
        author.setActuality(Actuality.REMOVED);
        authorRepository.save(author);
        return "deleted";
    }

    @PostMapping("/authors/{id}")
    public BookDto addBook(@PathVariable Long id, @RequestBody @Valid BookDto dto) throws AuthorContainsBookException {
        var author = authorRepository.findAuthorByIdAndActuality(id, Actuality.ACTIVE)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        author.addBook(dto.toBook());
        return modelMapper.map(authorRepository.save(author).getBooks().stream().filter(e -> e.equals(dto.toBook())).toList().get(0), BookDto.class);
    }

    @PatchMapping("/authors/{id}/books")
    public BookDto changeBook(@PathVariable Long id, @RequestBody @Valid BookDto dto) {
        var books = bookRepository.checkIdAuthorContainsBooksWithSameTitle(id, dto.getId(), dto.getTitle());
        if(books.size() > 1) {
            throw new AuthorContainsBookException();
        }
        var changed = bookRepository.checkIfAuthorContainsBookById(dto.getId(), id)
                .orElseThrow(AuthorDoesntContainsBookException::new);
        changed.setTitle(dto.getTitle());
        changed.setDescription(dto.getDescription());
        return modelMapper.map(bookRepository.save(changed), BookDto.class);
    }

    @DeleteMapping("/authors/{authorsId}/books/{bookId}")
    public String deleteBook(@PathVariable Long authorsId, @PathVariable Long bookId) {
        var author = authorRepository.findAuthorByIdAndActuality(authorsId, Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(authorsId));
        var book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        author.removeBook(book);
        authorRepository.save(author);
        return "deleted";
    }

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookRepository.getBooksWithAuthors().stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }

    @GetMapping("/books/{id}")
    public List<AuthorDto> getAuthors(@PathVariable Long id) {
        var authors = authorRepository.getAuthorsOfBook(id);
        if(authors == null) {
            throw new BookNotFoundException(id);
        }
        return authors.stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }
    
}
