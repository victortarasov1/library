package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(LibraryRestController.class)
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
class RestExceptionHandlerTest {
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorService authorService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mock;
    @Test
    void handleEntityNotFoundEx() throws Exception {
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenThrow(new AuthorNotFoundException(1L));
        this.mock.perform(get("/library/authors/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("debugMessage", equalTo("Author is not found, id=1")));
    }

    @Test
    void handleInvalidArgument() throws Exception {
        var dto = new AuthorDto();

        this.mock.perform(post("/library/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", equalTo("validation error")));
    }

    @Test
    void handleHttpMessageNotReadable() throws Exception{
        this.mock.perform(patch("/library/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("ddd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", equalTo("Malformed JSON Request")));
    }

    @Test
    void handleMethodArgumentTypeMismatchException() throws Exception {
        this.mock.perform(get("/library/authors/ddd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", equalTo("The parameter 'participantId' of value 'ddd' could not be converted to type 'Long'")));
    }

    @Test
    void handleNoHandlerFoundException() throws Exception {
        this.mock.perform(get("/ddd"))
                .andExpect(status().isNotFound());
    }
}