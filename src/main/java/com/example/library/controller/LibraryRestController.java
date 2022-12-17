package com.example.library.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.example.library.exception.*;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
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
@AllArgsConstructor
public class LibraryRestController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @GetMapping
    public List<AuthorDto> findAll() {
        return authorRepository.findAllByActuality(Actuality.ACTIVE).stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }


    @PostMapping("/authors")
    public AuthorDto addAuthor(@RequestBody @Valid AuthorDto dto) {
        if (authorRepository.findEqualsAuthors(dto.getEmail(), dto.getName(), dto.getSecondName(), 0L).isPresent()) {
            throw new AuthorNotUniqueException();
        }
        var author = dto.toAuthor();
        return modelMapper.map(authorRepository.save(author), AuthorDto.class);
    }


    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookRepository.getBooksWithAuthors().stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }

    @GetMapping("/books/{id}")
    public List<AuthorDto> getAuthors(@PathVariable Long id) {
        var authors = authorRepository.getAuthorsOfBook(id);
        if (authors == null) {
            throw new BookNotFoundException(id);
        }
        return authors.stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }

}
