package com.example.library.controller;

import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.AuthorNotUniqueException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder encoder;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/books")
    public List<BookDto> getAuthorBooks(Principal principal) {
        return bookRepository.getBooksByAuthorEmail(principal.getName()).stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public AuthorFullDto getAuthor(Principal principal) {
        var author = authorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        author.setPassword(null);
        return modelMapper.map(author, AuthorFullDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping
    public AuthorFullDto changeAuthor(Principal principal, @RequestBody @Valid AuthorFullDto dto) {
        var author = authorRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
        author.setAge(dto.getAge());
        author.setSecondName(dto.getSecondName());
        author.setName(dto.getName());
        author.setEmail(dto.getEmail());
        author.setPassword(encoder.encode(dto.getPassword()));
        if (authorRepository.findAuthorByEmailAndNameAndSecondNameAndIdNot(author.getEmail(), author.getName(), author.getSecondName(), author.getId()).isPresent()) {
            throw new AuthorNotUniqueException();
        }
        var updated = authorRepository.save(author);
        updated.setPassword(null);
        return modelMapper.map(updated, AuthorFullDto.class);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping
    public void deleteAuthorById(Principal principal) {
        var author = authorRepository.findByEmail(principal.getName()).orElseThrow(() -> new AuthorNotFoundException(principal.getName()));
     //   author.setActuality(Actuality.REMOVED);
        authorRepository.save(author);
    }
}
