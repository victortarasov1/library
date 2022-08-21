package com.example.library.service;



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

import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
	
	
	@Mock private AuthorRepository authorRepository;
	private AutoCloseable autoCloseable;
	private AuthorServiceImpl underTest;
	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new AuthorServiceImpl(authorRepository);
		
	}
	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}
	@Test
	public void findAllAuthorsTest() {
		//when
		underTest.findAll();
		//then
		verify(authorRepository).findAll();
	}
	
	@Test
	public void findByIdTest() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
		//when
		Author findedAuthor = underTest.findById((long) 1);
		//then
		assertEquals(findedAuthor, author);
	}
	
	@Test
	public void findByIdExceptionTest() {
		//given
		Long id = (long) 1;
		when(authorRepository.findById(id)).thenThrow(new AuthorNotFoundException(id));
		//when
		//then
		assertThatThrownBy(() -> underTest.findById(id)).isInstanceOf(AuthorNotFoundException.class).hasMessageContaining("Author is not found, id=" + id);
	}
	
	@Test
	public void changeAuthorTest() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		//when
		underTest.changeAuthor(author);
		//then
		verify(authorRepository).findById(author.getId());
		verify(authorRepository).save(author);
	}
	
	@Test
	public void deleteByIdTest() {
		//given
		Long id = (long) 1;
		doNothing().when(authorRepository).deleteById(id);
		//when
		underTest.deleteById(id);
		verify(authorRepository).deleteById(id);
	}
	
	@Test
	public void deleteByIdExceptionTest() {
		//given
		Long id = (long) 1;
		doThrow(new AuthorNotFoundException(id)).when(authorRepository).deleteById(id);
		//when
		//then
		assertThatThrownBy(() -> underTest.deleteById(id)).isInstanceOf(AuthorNotFoundException.class).hasMessageContaining("Author is not found, id=" + id);
		
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
		when(authorRepository.findAll()).thenReturn(Arrays.asList(author));
		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		//when
		Author savedAuthor =  underTest.save(author);
		assertEquals(author, savedAuthor);
		//authorRepository.save(author);
	}
	/*
	 * find equals element in  list and returns it
	 */
	@Test
	public void findEqualsTest() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		Author author_2 = new Author();
		author_2.setId((long) 2);
		author_2.setAge(30);
		author_2.setName("name");
		author_2.setSecondName("second name");
		author_2.setBooks(new ArrayList<Book>());
		when(authorRepository.findAll()).thenReturn(Arrays.asList(author_2));
		//when
		Author equalAuthor = underTest.findEquals(author);
		assertEquals(author_2, equalAuthor);
	}
	/*
	 * dont find equals element in list, and returns original element
	 */
	@Test
	public void findEqualsTest2() {
		//given
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		Author author_2 = new Author();
		author_2.setId((long) 2);
		author_2.setAge(30);
		author_2.setName("no name");
		author_2.setSecondName("no second name");
		author_2.setBooks(new ArrayList<Book>());
		when(authorRepository.findAll()).thenReturn(Arrays.asList(author_2));
		//when
		Author equalAuthor = underTest.findEquals(author);
		assertEquals(author, equalAuthor);
	}
	
	@Test
	public void addTitleTest() {
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
		when(authorRepository.findAll()).thenReturn(Arrays.asList(author));
		when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
		//when
		Author changedAuthor = underTest.addTitle(author.getId(), book);
		//author.addBook(book);
		assertEquals(changedAuthor, author );
		assertEquals(changedAuthor.getBooks().get(0), book);
		
	}
}
