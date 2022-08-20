package com.example.library.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.dto.BookFullDto;
import com.example.library.model.Author;
import com.example.library.service.AuthorService;

import com.example.library.service.BookService;

@RestController
@RequestMapping("/library")
@CrossOrigin(origins="*")
public class LibraryRestController {
	@Autowired
	AuthorService authorService;
	@Autowired
	BookService bookService;
    @Autowired
    private ModelMapper modelMapper;
	/*
	 * get all authors
	 */
	@GetMapping("/authors")
	public ResponseEntity <List <AuthorDto>> findAll(){
		
		return ResponseEntity.ok(authorService.findAll().stream().map( author -> modelMapper.map(author, AuthorDto.class)).collect(Collectors.toList()));
	}
	/*
	 * get author by id
	 */
	@GetMapping("/authors/{id}")
	public ResponseEntity<AuthorFullDto> findById(@PathVariable Long id){
		return ResponseEntity.ok(modelMapper.map(authorService.findById(id), AuthorFullDto.class));
	}
	/*
	 * change author
	 */
	@PatchMapping("/authors")
	public ResponseEntity <AuthorFullDto> changeAuthor(@RequestBody @Valid AuthorFullDto author){
		authorService.changeAuthor(author.toAuthor());
		return ResponseEntity.ok(author);

	}
	/*
	 * add author
	 */
	@PostMapping("/authors")
	/*public ResponseEntity <AuthorFullDto> addAuthor(@RequestBody @Valid AuthorFullDto author) {
		//return ResponseEntity.ok(modelMapper.map(authorService.save(author.toAuthor()), AuthorFullDto.class));
		//Author auth = authorService.save(author.toAuthor());
		//AuthorFullDto addedauthor = modelMapper.map(author, AuthorFullDto.class);
		AuthorFullDto addedauthor = modelMapper.map(authorService.save(author.toAuthor()), AuthorFullDto.class);
		//System.out.println(addedauthor.getBooks());
		return ResponseEntity.ok(addedauthor);
	}*/
	public ResponseEntity <Author> addAuthor(@RequestBody AuthorFullDto author){
		Author auth = authorService.save(author.toAuthor());
		//System.out.println(auth.getAge());
		AuthorFullDto addedauthor = modelMapper.map(auth, AuthorFullDto.class);
		return ResponseEntity.ok(auth);
	}
	/*
	 * delete author
	 */
	@DeleteMapping("/authors/{id}") 
	public ResponseEntity <String> deleteAuthorById(@PathVariable Long id){
		authorService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body("deleted");
	}
	/*
	 * delete book from author
	 */
	@DeleteMapping("/books/{id}")
	public ResponseEntity <String> deleteBookById(@PathVariable Long id){
		bookService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body("deleted");
	}
	/*
	 * add book to author
	 */
	@PostMapping("/authors/{id}")
	public ResponseEntity<AuthorFullDto> addBookTitle (@PathVariable Long id, @RequestBody @Valid BookDto book){
		return ResponseEntity.ok(modelMapper.map(authorService.addTitle(id, book.toBook()), AuthorFullDto.class));
	}
	/*
	 * get book by id
	 */
	@GetMapping("/books/{id}")
	public ResponseEntity<BookFullDto> findBookById(@PathVariable Long id){
		return ResponseEntity.ok(modelMapper.map(bookService.findById(id), BookFullDto.class));
	}
	/*
	 * add book
	 */
	@PostMapping("/books")
	public ResponseEntity<BookFullDto> addBook(@RequestBody @Valid BookFullDto book){
		return ResponseEntity.ok(modelMapper.map(bookService.save(book.toBook()), BookFullDto.class));
	}
	/*
	 * change book
	 */
	@PatchMapping("/books")
	public ResponseEntity<BookFullDto> changeBook(@RequestBody @Valid BookFullDto book){
		bookService.changeBook(book.toBook());
		return ResponseEntity.ok(book);
	}
	/*
	 * get all book
	 */
	@GetMapping("/books")
	public ResponseEntity<List<BookDto>> getBooks(){
		return ResponseEntity.ok(bookService.findAll().stream().map( book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList()));
	}
}
