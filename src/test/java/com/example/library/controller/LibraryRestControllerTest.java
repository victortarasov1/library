package com.example.library.controller;

import com.example.library.dto.AuthorDto;
import com.example.library.dto.BookDto;
import com.example.library.model.Actuality;
import com.example.library.model.Author;
import com.example.library.model.Book;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LibraryRestController.class)
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class})
class LibraryRestControllerTest {
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
    void findAll() throws Exception {
        var author = new Author(1L, "name", "second name", 33, Actuality.ACTIVE, null);
        var dto = new AuthorDto(1L, "name", "second name", 33);
        when(authorRepository.findAllByActuality(Actuality.ACTIVE)).thenReturn(List.of(author));
        when(modelMapper.map(author, AuthorDto.class)).thenReturn(dto);
        this.mock.perform(get("/library/authors")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",equalTo("name")))
                .andExpect(jsonPath("$[0].secondName", equalTo("second name")));
    }

    @Test
    void findById() throws Exception {
        var book = new Book();
        var dto = new BookDto(1L, "title", "description");
        var author = new Author(1L, "name", "second name", 33, Actuality.ACTIVE, List.of(book));
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenReturn(Optional.of(author));
        when(modelMapper.map(book,BookDto.class)).thenReturn(dto);
        this.mock.perform(get("/library/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id",equalTo(1)))
                .andExpect(jsonPath("$[0].title",equalTo("title")))
                .andExpect(jsonPath("$[0].description",equalTo("description")));
    }

    @Test
    void changeAuthor() throws Exception {
        var author = new Author();
        var dto =  new AuthorDto(1L, "name", "second name", 33);
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(modelMapper.map(author, AuthorDto.class)).thenReturn(dto);
        this.mock.perform(patch("/library/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name",equalTo("name")))
                .andExpect(jsonPath("secondName", equalTo("second name")))
                .andExpect(jsonPath("id",equalTo(1)))
                .andExpect(jsonPath("age",equalTo(33)));
    }

    @Test
    void addAuthor() throws Exception {
        var dto =  new AuthorDto(1L, "name", "second name", 33);
        var author = new Author(null, "name", "second name", 33, Actuality.ACTIVE, null);
        when(authorRepository.save(author)).thenReturn(author);
        when(modelMapper.map(author, AuthorDto.class)).thenReturn(dto);
        this.mock.perform(post("/library/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name",equalTo("name")))
                .andExpect(jsonPath("secondName", equalTo("second name")))
                .andExpect(jsonPath("age",equalTo(33)));
    }

    @Test
    void deleteAuthorById() throws Exception {
        var author = new Author();
        when(authorRepository.save(author)).thenReturn(author);
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenReturn(Optional.of(author));
        MvcResult res = this.mock.perform(delete("/library/authors/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andReturn();
        assertEquals(res.getResponse().getContentAsString(),"deleted");

    }

    @Test
    void addBook() throws Exception {
        var author = new Author();
        var book = new Book(1L,"title", "description", null);
        var dto = new BookDto(1L, "title", "description");
        when(authorService.addBook(author, dto.toBook())).thenReturn(List.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(dto);
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenReturn(Optional.of(author));
        this.mock.perform(post("/library/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title",equalTo("title")))
                .andExpect(jsonPath("description", equalTo("description")));
    }

    @Test
    void changeBook() throws Exception {

        var book = new Book(1L,"title", "description", null);
        var dto = new BookDto(1L, "title", "description");
        var author = new Author();
        author.addBook(book);
        when(authorService.addBook(author, dto.toBook())).thenReturn(List.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(dto);
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenReturn(Optional.of(author));
        this.mock.perform(patch("/library/authors/1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title",equalTo("title")))
                .andExpect(jsonPath("description", equalTo("description")));
    }

    @Test
    void deleteBook() throws Exception {
        var author = new Author();
        var book = new Book();
        when(authorRepository.findAuthorByIdAndActuality(1L, Actuality.ACTIVE)).thenReturn(Optional.of(author));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.save(author)).thenReturn(author);
        MvcResult res = this.mock.perform(delete("/library/authors/1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andReturn();
        assertEquals(res.getResponse().getContentAsString(),"deleted");
    }

    @Test
    void getBooks() throws Exception{
        var book = new Book(1L,"title", "description", List.of(new Author()));
        var dto = new BookDto(1L, "title", "description");
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(dto);
        this.mock.perform(get("/library/books")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title",equalTo("title")))
                .andExpect(jsonPath("$[0].description", equalTo("description")));
    }

    @Test
    void getAuthors() throws Exception{
        var book = new Book();
        var dto =  new AuthorDto(1L, "name", "second name", 33);
        var author = new Author(null, "name", "second name", 33, Actuality.ACTIVE, null);
        book.setAuthors(List.of(author));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(author, AuthorDto.class)).thenReturn(dto);
        this.mock.perform(get("/library/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",equalTo("name")))
                .andExpect(jsonPath("$[0].secondName", equalTo("second name")));
    }
}