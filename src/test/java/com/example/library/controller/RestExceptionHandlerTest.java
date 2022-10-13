package com.example.library.controller;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.library.dto.AuthorFullDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Author;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class RestExceptionHandlerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	AuthorService authorService;
	@MockBean
	BookService bookService;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	private ModelMapper modelMapper;
	
	@Test
	public void  handleEntityNotFoundExTest() throws Exception {
		Mockito.when(authorService.findById((long) 1)).thenThrow(new AuthorNotFoundException((long) 1));
		Mockito.when(bookService.findById((long) 1)).thenThrow(new BookNotFoundException((long) 1));
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
		Author author = dto.toAuthor();
		//Mockito.when(dto.toAuthor()).thenReturn(author);
		Mockito.when(authorService.save(author)).thenReturn(author);
		Mockito.when(modelMapper.map(author, AuthorFullDto.class)).thenReturn(dto);
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
		Author author = dto.toAuthor();
		//Mockito.when(dto.toAuthor()).thenReturn(author);
		Mockito.when(authorService.save(author)).thenReturn(author);
		Mockito.when(modelMapper.map(author, AuthorFullDto.class)).thenReturn(dto);
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
