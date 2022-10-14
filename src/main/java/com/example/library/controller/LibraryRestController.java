package com.example.library.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.AuthorActuality;
import com.example.library.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
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
import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.dto.BookFullDto;

@RestController
@RequestMapping("/library")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class LibraryRestController {
    private ModelMapper modelMapper;
    private AuthorRepository authorRepository;

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDto>> findAll() {
        var authors = authorRepository.findAllByAuthorActuality(AuthorActuality.ACTIVE);
        return ResponseEntity.ok(authors.stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList());
    }


    @GetMapping("/authors/{id}")
    public ResponseEntity<List<BookDto>> findById(@PathVariable Long id) {
        var author = authorRepository.findAuthorByIdAndAuthorActuality(id, AuthorActuality.ACTIVE).orElseThrow(()-> new AuthorNotFoundException(id));
        return ResponseEntity.ok( author.getBooks().stream().map(book -> modelMapper.map(book, BookDto.class)).toList());
    }

    @PatchMapping("/authors")
    public ResponseEntity<AuthorDto> changeAuthor(@RequestBody @Valid AuthorDto dto) {
        var author = authorRepository.findAuthorByIdAndAuthorActuality(dto.getId(), AuthorActuality.ACTIVE)
                .orElseThrow(()-> new AuthorNotFoundException(dto.getId()));
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
        var author = authorRepository.findAuthorByIdAndAuthorActuality(id, AuthorActuality.ACTIVE).orElseThrow(()-> new AuthorNotFoundException(id));
        author.setAuthorActuality(AuthorActuality.REMOVED);
        authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }
    @PostMapping("/authors/{id}")
    public ResponseEntity<BookDto> addBook(@PathVariable Long id,@RequestBody  @Valid BookDto dto){
        var author  = authorRepository.findAuthorByIdAndAuthorActuality(id, AuthorActuality.ACTIVE).orElseThrow(() -> new AuthorNotFoundException(id));
        author.addBook(dto.toBook());
        var a = authorRepository.save(author);
        var books = authorRepository.save(author).getBooks();
        return ResponseEntity.ok(modelMapper.map(books.get(books.size() -1), BookDto.class));
    }
//
//
//    @DeleteMapping("/books/{id}")
//    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
//        bookService.deleteById(id);
//        return ResponseEntity.status(HttpStatus.OK).body("deleted");
//    }
//
//    /*
//     * add book to author
//     */
//    @PostMapping("/authors/{id}")
//    public ResponseEntity<AuthorFullDto> addBookTitle(@PathVariable Long id, @RequestBody @Valid BookDto book) {
//        return ResponseEntity.ok(modelMapper.map(authorService.addTitle(id, book.toBook()), AuthorFullDto.class));
//    }
//
//    /*
//     * get book by id
//     */
//    @GetMapping("/books/{id}")
//    public ResponseEntity<BookFullDto> findBookById(@PathVariable Long id) {
//        return ResponseEntity.ok(modelMapper.map(bookService.findById(id), BookFullDto.class));
//    }
//
//    /*
//     * add book
//     */
//    @PostMapping("/books")
//    public ResponseEntity<BookFullDto> addBook(@RequestBody @Valid BookFullDto book) {
//        return ResponseEntity.ok(modelMapper.map(bookService.save(book.toBook()), BookFullDto.class));
//    }
//
//    /*
//     * change book
//     */
//    @PatchMapping("/books")
//    public ResponseEntity<BookFullDto> changeBook(@RequestBody @Valid BookFullDto book) {
//        bookService.changeBook(book.toBook());
//        return ResponseEntity.ok(book);
//    }
//
//    /*
//     * get all book
//     */
//    @GetMapping("/books")
//    public ResponseEntity<List<BookDto>> getBooks() {
//        return ResponseEntity.ok(bookService.findAll().stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList()));
//    }
}
