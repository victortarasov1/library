package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorContainsBookException;
import com.example.library.exception.AuthorDoesntContainsBookException;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/library/books")
@AllArgsConstructor
public class BookRestController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public BookDto addBook(Principal principal, @RequestBody @Valid BookDto dto) throws AuthorContainsBookException {
        var author = authorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        author.addBook(dto.toBook());
        return modelMapper.map(authorRepository.save(author).getBooks().stream().filter(e -> e.equals(dto.toBook())).toList().get(0), BookDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{id}")
    public void addAuthor(Principal principal, @PathVariable Long id, @RequestBody @Valid EmailDto dto) {
        var author = authorRepository.findByEmail(dto.email)
                .orElseThrow(() -> new AuthorNotFoundException(dto.email));
        var book = bookRepository.checkIfAuthorContainsBookById(id, principal.getName())
                .orElseThrow(AuthorDoesntContainsBookException::new);
        author.addBook(book);
        authorRepository.save(author);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/authors/{id}")
    public List<AuthorDto> getBookAuthors(Principal principal, @PathVariable Long id) {
        return bookRepository.getBookAndAuthorsById(id)
                .orElseThrow(() -> new BookNotFoundException(id))
                .getAuthors().stream().filter(author -> !author.getEmail().equals(principal.getName()))
                .map(AuthorDto::new).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping
    public void changeBook(Principal principal, @RequestBody @Valid BookDto dto) {
        var author = authorRepository.findAuthorAndBooksByEmail(principal.getName())
                .orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        var book = author.getBooks().stream().filter(b -> b.getId().equals(dto.getId())).findFirst()
                .orElseThrow(() -> new BookNotFoundException(dto.getId()));
        if (!book.getTitle().equals(dto.getTitle()) && author.containsBook(dto.getTitle()))
            throw new AuthorContainsBookException();
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public void deleteBook(Principal principal, @PathVariable Long id) {
        var author = authorRepository.findByEmail(principal.getName()).orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        var book = bookRepository.checkIfAuthorContainsBookById(id, principal.getName()).orElseThrow(() -> new BookNotFoundException(id));
        author.removeBook(book);
        authorRepository.save(author);
    }

    record EmailDto(@NotNull(message = "email name mst be setted!") @Email String email) {
    }
}
