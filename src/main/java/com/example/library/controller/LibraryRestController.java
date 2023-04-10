package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorNotUniqueException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryRestController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;


    @GetMapping
    public List<AuthorDto> findAll() {
        return authorRepository.findAll(Actuality.ACTIVE).stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }


    @PostMapping
    public AuthorDto addAuthor(@RequestBody @Valid AuthorFullDto dto) {
        try {
            return modelMapper.map(authorRepository.save(dto.toAuthor()), AuthorDto.class);
        } catch (DataIntegrityViolationException ex) {
            throw new AuthorNotUniqueException();
        }
    }


    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookRepository.getBooks().stream().map(book -> modelMapper.map(book, BookDto.class)).toList();
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
