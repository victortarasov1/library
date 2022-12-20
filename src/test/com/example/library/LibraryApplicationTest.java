package com.example.library;

import com.example.library.controller.AuthorRestController;
import com.example.library.controller.BookRestController;
import com.example.library.controller.LibraryRestController;
import com.example.library.controller.RestExceptionHandler;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
class LibraryApplicationTest {

    @Autowired
    AuthorRestController authorRestController;
    @Autowired
    LibraryRestController libraryRestController;
    @Autowired
    BookRestController bookRestController;
    @Autowired
    RestExceptionHandler restExceptionHandler;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void contextLoads() {
        assertThat(libraryRestController).isNotNull();
        assertThat(authorRestController).isNotNull();
        assertThat(bookRestController).isNotNull();
        assertThat(restExceptionHandler).isNotNull();
        assertThat(encoder).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(authorRepository).isNotNull();
        assertThat(bookRepository).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

}