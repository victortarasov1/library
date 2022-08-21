package com.example.library.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	@Mock 
	private BookRepository bookRepository;
	@Mock 
	private AuthorService authorService;
	private AutoCloseable autoCloseable;
	private BookServiceImpl underTest;
	
	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new BookServiceImpl(bookRepository, authorService);
		
	}
	
	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}
	
	@Test
	public void findAllBooksTest() {
		//when
		underTest.findAll();
		//then
		verify(bookRepository).findAll();
	}
	
	@Test
	public void findByIdTest() {
		//given
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
		//when
		Book findedBook = underTest.findById((long) 1);
		//then
		assertEquals(findedBook, book);
	}
	
	@Test
	public void findByIdExceptionTest() {
		//given
		Long id = (long) 1;
		when(bookRepository.findById(id)).thenThrow(new BookNotFoundException(id));
		//when
		//then
		assertThatThrownBy(() -> underTest.findById(id)).isInstanceOf(BookNotFoundException.class).hasMessageContaining("Book is not found, id=" + id);
	}
	
	@Test
	public void deleteByIdTest() {
		//given
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
		doNothing().when(bookRepository).deleteById(book.getId());
		//when
		underTest.deleteById(book.getId());
		//then
		verify(bookRepository).deleteById(book.getId());
	}
	@Test
	public void deleteByIdExceptionTest() {
		//given
		Long id = (long) 1;
		assertThatThrownBy(() -> underTest.findById(id)).isInstanceOf(BookNotFoundException.class).hasMessageContaining("Book is not found, id=" + id);
		//when
		//then
		verify(bookRepository).findById(id);
	}
	
	@Test
	public void saveTest() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		author.addBook(book);
		when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
		//when
		Book savedBook = underTest.save(book);
		//then
		assertEquals(book, savedBook);
		verify(bookRepository).save(book);
	}
	//with adding new author
	@Test
	public void changeBookTest() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		book.setAuthor(author);
		when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
		//when
		underTest.changeBook(book);
		//then
		verify(bookRepository).save(book);
	}
	@Test
	public void changeBookTest_2() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		Author secondAuthor = new Author();
		secondAuthor.setId((long) 1);
		secondAuthor.setAge(30);
		secondAuthor.setName("no name");
		secondAuthor.setSecondName("no second name");
		secondAuthor.setBooks(new ArrayList<Book>());
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		book.setAuthor(author);
		Book secondBook = new Book();
		secondBook.setId((long) 1);
		secondBook.setTitle("title");
		secondBook.setDescription("description");
		secondBook.setAuthor(secondAuthor);
		when(bookRepository.findById(book.getId())).thenReturn(Optional.of(secondBook));
		when(authorService.findEquals(author)).thenReturn(author);
		//when
		underTest.changeBook(book);
		//then
		verify(bookRepository).save(book);
	}
	
	@Test
	public void findEqualsTest() {
		//given
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		Book secondBook = new Book();
		secondBook.setId((long) 1);
		secondBook.setTitle("no title");
		secondBook.setDescription("no description");
		when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
		//when
		Book equalsBook = underTest.findEquals(book);
		//then
		assertEquals(equalsBook, book);
	}
	
	@Test
	public void findEqualsTest_2() {
		//given
		Book book = new Book();
		book.setId((long) 1);
		book.setTitle("title");
		book.setDescription("description");
		Book secondBook = new Book();
		secondBook.setId((long) 1);
		secondBook.setTitle("title");
		secondBook.setDescription("description");
		when(bookRepository.findAll()).thenReturn(Arrays.asList(secondBook));
		//when
		Book equalsBook = underTest.findEquals(book);
		//then
		assertEquals(equalsBook, book);
	}
}
