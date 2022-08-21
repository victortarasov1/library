package com.example.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.library.controller.LibraryRestController;
import com.example.library.dto.AuthorDto;
import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.dto.BookFullDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class LibraryApplicationTest {
	@Autowired
	private LibraryRestController restController;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@Test
	public void contextLoads() {
		assertThat(restController).isNotNull();
	}
	@BeforeEach
	public void addData() {
		Author author = new Author();
		author.setAge(30);
		author.setName("name");
		author.setSecondName("second name");
		author.setBooks(new ArrayList<Book> ());
		Book book = new Book();
		book.setAuthor(author);
		book.setDescription("description");
		book.setTitle("title");
		bookRepository.save(book);
	}
	@AfterEach
	public void resetDb() {
		authorRepository.deleteAll();
		bookRepository.deleteAll();
	}
	@Test
	public void getAuthorsTest() throws Exception {
		//when
		this.mockMvc.perform(get("/library/authors"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", equalTo("name")))
				.andExpect(jsonPath("$[0].secondName", equalTo("second name")));
        
                		
	}
	@Test
	public void getAuthorByIdTest() throws Exception {
		Long id = authorRepository.findAll().get(0).getId();
		this.mockMvc.perform(get("/library/authors/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("id", equalTo(id.intValue())))
			.andExpect(jsonPath("name", equalTo("name")))
			.andExpect(jsonPath("secondName", equalTo("second name")))
			.andExpect(jsonPath("age", equalTo(30)))
			.andExpect(jsonPath("books",hasSize(1)))
			.andExpect(jsonPath("books[0].title", equalTo("title")));
		
	}
	@Test
	public void addAuthorTest() throws Exception {
		int id = authorRepository.findAll().get(0).getId().intValue();
		id += 1;
		AuthorFullDto dto = new AuthorFullDto();
		dto.setName("new name");
		dto.setAge(50);
		dto.setSecondName("new second name");
		this.mockMvc.perform(post("/library/authors")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				//.andDo(print())
				.andExpect(jsonPath("id", equalTo(id)))
				.andExpect(jsonPath("age", equalTo(50)))
				.andExpect(jsonPath("name", equalTo("new name")))
				.andExpect(jsonPath("secondName", equalTo("new second name")));
	}
	@Test
	public void changeAuthorTest() throws Exception {
		Long id = authorRepository.findAll().get(0).getId();
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId(id);
		dto.setAge(30);
		dto.setName("new name");
		dto.setSecondName("new second name");
		BookDto bookDto = new BookDto();
		bookDto.setTitle("new title");
		dto.setBooks(Arrays.asList(bookDto));
		this.mockMvc.perform(patch("/library/authors")
		            .contentType(MediaType.APPLICATION_JSON)
		            	.content(objectMapper.writeValueAsString(dto)))
							.andExpect(status().isOk())
							.andExpect(content().contentType(MediaType.APPLICATION_JSON))
							.andExpect(jsonPath("id", equalTo(id.intValue())))
							.andExpect(jsonPath("name", equalTo("new name")))
							.andExpect(jsonPath("secondName", equalTo("new second name")))
							.andExpect(jsonPath("books",hasSize(1)))
							.andExpect(jsonPath("books[0].title", equalTo("new title")));
	}
	@Test
	public void deleteAuthorByIdTest() throws Exception {
		Long id = authorRepository.findAll().get(0).getId();
		MvcResult res = this.mockMvc.perform(delete("/library/authors/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
			.andReturn();
		assertEquals(res.getResponse().getContentAsString(),"deleted");
	}
	@Test
	public void deleteBookByIdTest() throws Exception {
		Long id = bookRepository.findAll().get(0).getId();
		MvcResult res = this.mockMvc.perform(delete("/library/books/" + id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
				.andReturn();
			assertEquals(res.getResponse().getContentAsString(),"deleted");
	}
	@Test
	public void addBookTitleTest() throws Exception {
		Long id = authorRepository.findAll().get(0).getId();
		BookDto book = new BookDto();
		book.setTitle("any title");
		this.mockMvc.perform(post("/library/authors/" + id)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				//.andDo(print())
				.andExpect(jsonPath("id", equalTo(id.intValue())))
				.andExpect(jsonPath("name", equalTo("name")))
				.andExpect(jsonPath("secondName", equalTo("second name")))
				.andExpect(jsonPath("books",hasSize(2)))
				.andExpect(jsonPath("books[0].title", equalTo("title")))
				.andExpect(jsonPath("books[1].title", equalTo("any title")));
	}
	@Test
	public void getBookByIdTest() throws Exception {
		Long id = bookRepository.findAll().get(0).getId();
		this.mockMvc.perform(get("/library/books/"+id))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("id", equalTo(id.intValue())))
		.andExpect(jsonPath("title", equalTo("title")))
		.andExpect(jsonPath("description", equalTo("description")))
		.andExpect(jsonPath("author.name", equalTo("name")))
		.andExpect(jsonPath("author.secondName", equalTo("second name")));
	}
	@Test
	public void changeBookTest() throws Exception {
		Long id = bookRepository.findAll().get(0).getId();
		Long authorId = authorRepository.findAll().get(0).getId();
		AuthorDto authorDto = new AuthorDto();
		authorDto.setName("name");
		authorDto.setSecondName("second Name");
		authorDto.setId(authorId);
		BookFullDto bookDto = new BookFullDto();
		bookDto.setId(id);
		bookDto.setTitle("title");
		bookDto.setDescription("description");
		bookDto.setAuthor(authorDto);
		this.mockMvc.perform(patch("/library/books")
	            .contentType(MediaType.APPLICATION_JSON)
	            	.content(objectMapper.writeValueAsString(bookDto)))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("id", equalTo(id.intValue())))
						.andExpect(jsonPath("title", equalTo("title")))
						.andExpect(jsonPath("description", equalTo("description")))
						.andExpect(jsonPath("author.name", equalTo("name")))
						.andExpect(jsonPath("author.secondName", equalTo("second Name")));
	}
	
	@Test
	public void getBooks() throws Exception {
		//Long id = bookRepository.findAll().get(0).getId();
		this.mockMvc.perform(get("/library/books"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
            	.andExpect(jsonPath("$", hasSize(1)))
                //.andExpect(jsonPath("$[0].id", equalTo(id.intValue())))
				.andExpect(jsonPath("$[0].title", equalTo("title")));
									
	}
	@Test
	public void addBookTest() throws Exception {
		Long id = bookRepository.findAll().get(0).getAuthor().getId();
		Author author = authorRepository.findAll().get(0);
		AuthorDto authorDto = new AuthorDto();
		authorDto.setId(author.getId());
		authorDto.setName(author.getName());
		authorDto.setSecondName(author.getSecondName());
		BookFullDto bookDto = new BookFullDto();
		bookDto.setTitle("new title");
		bookDto.setDescription("description");
		bookDto.setAuthor(authorDto);
		this.mockMvc.perform(post("/library/books")
		  		.contentType(MediaType.APPLICATION_JSON)
		  		.content(objectMapper.writeValueAsString(bookDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("title", equalTo("new title")))
				.andExpect(jsonPath("description", equalTo("description")))
				.andExpect(jsonPath("author.name", equalTo("name")))
				.andExpect(jsonPath("author.secondName", equalTo("second name")));
				
				
	}
	@Test
	public void addBookWithNewAuthorTest() throws Exception {
		AuthorDto authorDto = new AuthorDto();
		authorDto.setName("name");
		authorDto.setSecondName("second name");
		BookFullDto bookDto = new BookFullDto();
		bookDto.setTitle("new title");
		bookDto.setDescription("description");
		bookDto.setAuthor(authorDto);
		this.mockMvc.perform(post("/library/books")
		  		.contentType(MediaType.APPLICATION_JSON)
		  		.content(objectMapper.writeValueAsString(bookDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("title", equalTo("new title")))
				.andExpect(jsonPath("description", equalTo("description")))
				.andExpect(jsonPath("author.name", equalTo("name")))
				.andExpect(jsonPath("author.secondName", equalTo("second name")));
				
				
	}
	@Test
	public void  handleEntityNotFoundExTest() throws Exception {
		this.mockMvc.perform(get("/library/authors/1"))
			.andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("debugMessage", equalTo("Author is not found, id=1")));
		this.mockMvc.perform(get("/library/books/1"))
			.andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("debugMessage", equalTo("Book is not found, id=1")));
							
	}
	@Test
	public void handleInvalidArgument() throws Exception {
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId((long)1);
		dto.setAge(30);
		dto.setName("name");
		dto.setSecondName("second name");
		dto.setAge(400);
		this.mockMvc.perform(patch("/library/authors")
	            .contentType(MediaType.APPLICATION_JSON)
            	.content(objectMapper.writeValueAsString(dto)))
		 		.andExpect(status().isBadRequest())
		 		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message", equalTo("validation error")));
				//.andDo(print());
	}
	@Test
	public void HttpMessageNotReadableExceptionTest() throws Exception {
		this.mockMvc.perform(patch("/library/authors")
	            .contentType(MediaType.APPLICATION_JSON)
            	.content("ddd"))
		 		.andExpect(status().isBadRequest())
		 		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message", equalTo("Malformed JSON Request")));
				//.andDo(print());
	}
	
	@Test
	public void MethodArgumentTypeMismatchExceptionTest() throws Exception {
		AuthorFullDto dto = new AuthorFullDto();
		dto.setId((long)1);
		dto.setAge(30);
		dto.setName("name");
		dto.setSecondName("second name");
		dto.setAge(400);
		this.mockMvc.perform(get("/library/authors/ddd"))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("message", equalTo("The parameter 'id' of value 'ddd' could not be converted to type 'Long'")));

	}
	
	@Test
	public void handleNoHandlerFoundExceptionTest() throws Exception {
		this.mockMvc.perform(get("/library/ddd"))
			.andExpect(status().isNotFound());
	}
}
