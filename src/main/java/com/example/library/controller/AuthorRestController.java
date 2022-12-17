package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.AuthorNotUniqueException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/library/authors")
@AllArgsConstructor
public class AuthorRestController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<BookDto> getAuthorBooks(Principal principal) {
        return bookRepository.getBooksByAuthorEmail(principal.getName()).stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping
    public AuthorDto changeAuthor(Principal principal, @RequestBody @Valid AuthorDto dto) {
        var author = authorRepository.findAuthorByEmailAndActuality(dto.getEmail(), Actuality.ACTIVE)
                .orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        author.setAge(dto.getAge());
        author.setSecondName(dto.getSecondName());
        author.setName(dto.getName());
        if(authorRepository.findEqualsAuthors(author.getEmail(), author.getName(), author.getSecondName(), author.getId()).isPresent()) {
            throw new AuthorNotUniqueException();
        }
        var updated = authorRepository.save(author);
        return modelMapper.map(updated, AuthorDto.class);

    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping
    public String deleteAuthorById(Principal principal) {
        var author = authorRepository.findAuthorByEmailAndActuality(principal.getName(), Actuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        author.setActuality(Actuality.REMOVED);
        authorRepository.save(author);
        return "deleted";
    }
}
