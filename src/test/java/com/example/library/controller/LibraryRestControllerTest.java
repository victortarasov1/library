package com.example.library.controller;

import org.apache.catalina.mapper.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.library.dto.AuthorDto;
import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.dto.BookFullDto;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(LibraryRestController.class)
public class LibraryRestControllerTest {
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	AuthorService authorService;
	@MockBean
	BookService bookService;
	
	@MockBean
	private ModelMapper modelMapper;
	//Disable all @Valid annotations
	
	@MockBean
	private LocalValidatorFactoryBean validator;
	@Test
	public void getAuthorsTest() throws Exception {
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		AuthorDto dto = new AuthorDto();
		dto.setId(author.getId());
		dto.setName(author.getName());
		dto.setSecondName(author.getSecondName());
		Mockito.when(authorService.findAll()).thenReturn(Arrays.asList(author));
		Mockito.when(modelMapper.map(author, AuthorDto.class)).thenReturn(dto);
		this.mockMvc.perform(get("/library/authors"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
				.andExpect(jsonPath("$[0].name", equalTo("name")))
				.andExpect(jsonPath("$[0].secondName", equalTo("second name")));
                
                		
	}
	
	@Test
	public void getAuthorByIdTest() throws Exception {
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		BookDto bookDto = new BookDto();
		bookDto.setTitle("title");
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId(author.getId());
		dto.setAge(author.getAge());
		dto.setName(author.getName());
		dto.setSecondName(author.getSecondName());
		dto.setBooks(Arrays.asList(bookDto));
		Mockito.when(authorService.findById((long) 1)).thenReturn(author);
		Mockito.when(modelMapper.map(author, AuthorFullDto.class)).thenReturn(dto);
		this.mockMvc.perform(get("/library/authors/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("id", equalTo(1)))
			.andExpect(jsonPath("name", equalTo("name")))
			.andExpect(jsonPath("secondName", equalTo("second name")))
			.andExpect(jsonPath("age", equalTo(30)))
			.andExpect(jsonPath("books",hasSize(1)))
			.andExpect(jsonPath("books[0].title", equalTo("title")));
	}
	
	@Test
	public void changeAuthorTest() throws Exception {
		//BookDto bookDto = new BookDto();
		//bookDto.setTitle("new title");
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId((long) 1);
		dto.setAge(30);
		dto.setName("name");
		dto.setSecondName("second name");
		//dto.setBooks(Arrays.asList(bookDto));
		//Mockito.when(authorsService.changeAuthor(dto.toAuthor())).thenReturn()
		Mockito.doNothing().when(authorService).changeAuthor(dto.toAuthor());
		this.mockMvc.perform(patch("/library/authors")
		            .contentType(MediaType.APPLICATION_JSON)
		            	.content(objectMapper.writeValueAsString(dto)))
							.andExpect(status().isOk())
							.andExpect(content().contentType(MediaType.APPLICATION_JSON))
							.andExpect(jsonPath("id", equalTo(1)))
							.andExpect(jsonPath("name", equalTo("name")))
							.andExpect(jsonPath("secondName", equalTo("second name")));
							//.andExpect(jsonPath("books",hasSize(1)))
							//.andExpect(jsonPath("books[0].title", equalTo("title")));
	}
	
	@Test
	public void addAuthorTest() throws Exception {
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		
		/*BookDto bookDto = new BookDto();
		bookDto.setTitle("new title");*/
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId(author.getId());
		dto.setName(author.getName());
		dto.setAge(author.getAge());
		dto.setSecondName(author.getSecondName());
		//dto.setBooks(Arrays.asList(bookDto));
		//author.setBooks(Arrays.asList(bookDto.toBook()));
		Mockito.when(authorService.save(dto.toAuthor())).thenReturn(author);
		Mockito.when(modelMapper.map(author, AuthorFullDto.class)).thenReturn(dto);
		this.mockMvc.perform(post("/library/authors")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				//.andDo(print())
				.andExpect(jsonPath("id", equalTo(1)))
				.andExpect(jsonPath("name", equalTo("name")))
				.andExpect(jsonPath("secondName", equalTo("second name")));
				
	}
	@Test
	public void deleteAuthorByIdTest() throws Exception {
		Mockito.doNothing().when(authorService).deleteById((long) 1);
		
		MvcResult res = this.mockMvc.perform(delete("/library/authors/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
			.andReturn();
		assertEquals(res.getResponse().getContentAsString(),"deleted");
	}
	
	@Test
	public void deleteBookByIdTest() throws Exception {
		Mockito.doNothing().when(bookService).deleteById((long) 1);
		MvcResult res = this.mockMvc.perform(delete("/library/books/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
				.andReturn();
			assertEquals(res.getResponse().getContentAsString(),"deleted");
	}
	
	@Test
	public void addBookTitleTest() throws Exception {
		
		BookDto book = new BookDto();
		//book.setId((long) 1);
		book.setTitle("title");
		Author author = new Author();
		author.setId((long) 1);
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(Arrays.asList(book.toBook()));
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId(author.getId());
		dto.setName(author.getName());
		dto.setSecondName(author.getSecondName());
		dto.setBooks(Arrays.asList(book));
		Mockito.when(authorService.addTitle((long)1, book.toBook())).thenReturn(author);
		Mockito.when(modelMapper.map(author, AuthorFullDto.class)).thenReturn(dto);
		this.mockMvc.perform(post("/library/authors/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				//.andDo(print())
				.andExpect(jsonPath("id", equalTo(1)))
				.andExpect(jsonPath("name", equalTo("name")))
				.andExpect(jsonPath("secondName", equalTo("second name")))
				.andExpect(jsonPath("books",hasSize(1)))
				.andExpect(jsonPath("books[0].title", equalTo("title")));
		
	}
	
	@Test
	public void getBookByIdTest() throws Exception {
		AuthorDto authorDto = new AuthorDto();
		authorDto.setId((long) 1);
		authorDto.setName("name");
		authorDto.setSecondName("second name");
		BookFullDto bookDto = new BookFullDto();
		bookDto.setId((long) 1);
		bookDto.setTitle("title");
		bookDto.setDescription("description");
		bookDto.setAuthor(authorDto);
		Book book = bookDto.toBook();
		
		Mockito.when(bookService.findById((long)1)).thenReturn(book);
		Mockito.when(modelMapper.map(book, BookFullDto.class)).thenReturn(bookDto);
		this.mockMvc.perform(get("/library/books/1"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("id", equalTo(1)))
		.andExpect(jsonPath("title", equalTo("title")))
		.andExpect(jsonPath("description", equalTo("description")))
		.andExpect(jsonPath("author.name",equalTo("name")))
		.andExpect(jsonPath("author.secondName",equalTo("second name")));
	}
	
	@Test
	public void changeBookTest() throws Exception {
		AuthorDto authorDto = new AuthorDto();
		authorDto.setId((long) 1);
		authorDto.setName("name");
		authorDto.setSecondName("second name");
		BookFullDto bookDto = new BookFullDto();
		bookDto.setId((long) 1);
		bookDto.setTitle("title");
		bookDto.setDescription("description");
		bookDto.setAuthor(authorDto);
		Mockito.doNothing().when(bookService).changeBook(bookDto.toBook());
		this.mockMvc.perform(patch("/library/books")
	            .contentType(MediaType.APPLICATION_JSON)
	            	.content(objectMapper.writeValueAsString(bookDto)))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("id", equalTo(1)))
						.andExpect(jsonPath("title", equalTo("title")))
						.andExpect(jsonPath("description", equalTo("description")))
						.andExpect(jsonPath("author.name",equalTo("name")))
						.andExpect(jsonPath("author.secondName",equalTo("second name")));
	}
	
	@Test
	public void getBooks() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setTitle("title");
		Book book = bookDto.toBook();
		Mockito.when(bookService.findAll()).thenReturn(Arrays.asList(book));
		Mockito.when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
		this.mockMvc.perform(get("/library/books"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
            	.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].title", equalTo("title")));
									
	}
	@Test
	public void addBookTest() throws Exception {
		AuthorDto authorDto = new AuthorDto();
		//authorDto.setId((long) 1);
		authorDto.setName("name");
		authorDto.setSecondName("second name");
		BookFullDto bookDto = new BookFullDto();
		bookDto.setTitle("title");
		bookDto.setDescription("description");
		bookDto.setId((long) 1);
		bookDto.setAuthor(authorDto);
		Book book = bookDto.toBook();
		Mockito.when(bookService.save(book)).thenReturn(book);
		Mockito.when(modelMapper.map(book, BookFullDto.class)).thenReturn(bookDto);
		this.mockMvc.perform(post("/library/books")
		  		.contentType(MediaType.APPLICATION_JSON)
		  		.content(objectMapper.writeValueAsString(bookDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				//.andExpect(jsonPath("id", equalTo(1)))
				.andExpect(jsonPath("title", equalTo("title")))
				.andExpect(jsonPath("description", equalTo("description")))
				.andExpect(jsonPath("author.name", equalTo("name")))
				.andExpect(jsonPath("author.secondName", equalTo("second name")));
				
				
	}
}
