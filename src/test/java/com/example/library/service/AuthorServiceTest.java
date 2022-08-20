package com.example.library.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
	
	 @Mock
	AuthorRepository authorRepository;
	 @InjectMocks
	AuthorServiceImpl authorService;

	@Test
	public void findAllTest() {
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		List<Author> authors= new ArrayList<>();
		{
			authors.add(author);
		}
		//given(authorRepository.findAll()).willReturn(authors);
		when(authorRepository.findAll()).thenReturn(authors);
		List <Author> res = authorService.findAll();
		assertEquals(res.size(), authors.size());
		assertEquals(res.get(0).getId(), author.getId());
		assertEquals(res.get(0).getName(), author.getName());
		assertEquals(res.get(0).getSecondName(), author.getSecondName());
		
	}
	@Test
	public void changeAuthorTest() {
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		//doNothing().when(authorRepository).save(author);
		when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
		authorService.changeAuthor(author);
		//Mockito.verify(authorRepository,Mockito.times(1)).save(eq(author));
	}
	@Test
	public void findByIdTest() {
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
		Author findedAuthor = authorService.findById(author.getId());
		assertEquals(findedAuthor.getId(), author.getId());
		assertEquals(findedAuthor.getName(), author.getName());
		assertEquals(findedAuthor.getSecondName(), author.getSecondName());
	}
	
	@Test
	public void deleteByIdTest() {
		doNothing().when(authorRepository).deleteById((long) 1);
		authorRepository.deleteById((long) 1);
	}
	
	@Test
	public void saveTest() {
		final Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book>());
		when(authorRepository.save(author)).thenReturn(author);
		when(authorRepository.findAll()).thenReturn(Arrays.asList(author));
		when(authorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(author));
		Author savedAuthor = authorService.save(author);
		savedAuthor.setName("d");
		System.out.println(author.getName());
		assertEquals(savedAuthor.getId(), author.getId());
		assertEquals(savedAuthor.getName(), author.getName());
		assertEquals(savedAuthor.getSecondName(), author.getSecondName());
	}
}
