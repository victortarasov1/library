package com.example.library;

import com.example.library.controller.AuthorRestController;
import com.example.library.controller.BookRestController;
import com.example.library.controller.LibraryRestController;
import com.example.library.controller.RestExceptionHandler;
import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    AuthorFullDto creatAuthorDto(String email) {
        return new AuthorFullDto("name", "second name", 33, "112101", email);
    }
    @BeforeEach
    public void addData() {
        var firstAuthor = creatAuthorDto("first@gmail.com").toAuthor();
        var secondAuthor = creatAuthorDto("second@gmail.com").toAuthor();
        firstAuthor.setPassword(encoder.encode(firstAuthor.getPassword()));
        secondAuthor.setPassword(encoder.encode(secondAuthor.getPassword()));
        var firstBook = new BookDto(null, "title", "description").toBook();
        var secondBook = new BookDto(null, "second title", "description").toBook();
        firstAuthor.addBook(firstBook);
        secondAuthor.addBook(secondBook);
        authorRepository.save(firstAuthor);
        authorRepository.save(secondAuthor);
    }
    @AfterEach
    public void resetDb() {
        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void login () throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        //when
        var res = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
    }
    @Test
    void login_returnException () throws Exception {
        //given
        var email = "firs@gmail.com";
        var password = "112101";
        //then
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andExpect(jsonPath("errors", equalTo("bad password and/or email")));
    }

    @Test
    void regenerateAccessToken() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String refresh = JsonPath.parse(tokens.getResponse().getContentAsString()).read("refresh_token");
        //when
        var res = this.mockMvc.perform(get("/library/refresh")
                        .header("Authorization", "Bearer " + refresh))
                .andReturn();
        //then
        assertNotNull( JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token"));
    }

    @Test
    void findAllAuthors() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //then
        this.mockMvc.perform(get("/library")
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @Test
    void findAllBooks() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //then
        this.mockMvc.perform(get("/library/books")
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void findAllBookAuthors() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        var id = bookRepository.findAll().get(0).getId();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //then
        this.mockMvc.perform(get("/library/books/" + id)
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    void addAuthor() throws Exception {
        //when
        var dto = creatAuthorDto("changed@gmail.com");
        //then
        this.mockMvc.perform(post("/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("name", equalTo("name")))
                .andExpect(jsonPath("secondName", equalTo("second name")));
    }
    @Test
    void getAuthor() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //then
        this.mockMvc.perform(get("/library/authors")
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("email", equalTo(email)));
    }

    @Test
    void deleteAuthor() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //then
        this.mockMvc.perform(delete("/library/authors")
                        .header("Authorization", "Bearer " + access))
                .andExpect(status().isOk());
    }
    @Test
    void changeAuthor() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //when
        var dto = creatAuthorDto("changed@gmail.com");

        //then
        this.mockMvc.perform(patch("/library/authors")
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("email", equalTo(dto.getEmail())));
    }
    @Test
    public void getAuthorBooks() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //then
        this.mockMvc.perform(get("/library/authors/books")
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void addBook() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        //when
        var dto = new BookDto(null, "new title", "description");
        //then
        this.mockMvc.perform(post("/library/books")
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("description", equalTo(dto.getDescription())));
    }

    @Test
    public void addAuthorToBook() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        var id = bookRepository.findAll().get(0).getId();
        var dto = new EmailDto("second@gmail.com");
        //then
        this.mockMvc.perform(post("/library/books/" + id)
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());


    }

    @Test
    public void changeBook() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        var id = bookRepository.findAll().get(0).getId();
        var dto = new BookDto(id, "title", "description");
        this.mockMvc.perform(patch("/library/books")
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("description", equalTo(dto.getDescription())));
    }
    @Test
    public void deleteBook() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        var id = bookRepository.findAll().get(0).getId();
        //then
        this.mockMvc.perform(delete("/library/books/" + id)
                        .header("Authorization", "Bearer " + access))
                .andExpect(status().isOk());

    }

    @Test
    public void changeBook_shouldReturnExceptionAuthorDoesntContainsBook() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");

        var dto = new BookDto(0L, "title", "changed book");
        var errors = new String[] {"This data is not acceptable!", "author doesnt contains this book"};
        this.mockMvc.perform(patch("/library/books")
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("errors", equalTo(List.of(errors))));
    }


    @Test
    public void changeBook_shouldReturnExceptionMethodArgumentNotValid() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");
        var id = bookRepository.findAll().get(0).getId();
        var dto = new BookDto(id, "title", "");
        this.mockMvc.perform(patch("/library/books")
                        .header("Authorization", "Bearer " + access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("errors", equalTo(List.of("description must be between 10 and 100 literals"))));
    }
    @Test
    public void addAuthor_shouldReturnExceptionHandleHttpMessageNotReadable() throws Exception {
        //when
        var dto = creatAuthorDto("changed@gmail.com");
        //then
        this.mockMvc.perform(post("/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Fff"))
                .andExpect(jsonPath("message", equalTo("Malformed JSON Request")));
    }
    @Test
    public void deleteBook_shouldReturnExceptionHandleMethodArgumentTypeMismatch() throws Exception {
        //given
        var email = "first@gmail.com";
        var password = "112101";
        var tokens = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", password)
                        .param("email", email))
                .andReturn();
        String access = JsonPath.parse(tokens.getResponse().getContentAsString()).read("access_token");

        //then
        this.mockMvc.perform(delete("/library/books/" + "fff")
                        .header("Authorization", "Bearer " + access))
                .andExpect(jsonPath("message", equalTo("The parameter 'id' of value 'fff' could not be converted to type 'Long'")));

    }
    @Test
    void handleNoHandlerFoundException() throws Exception {
        this.mockMvc.perform(get("/ddd"))
                .andExpect(status().isNotFound());
    }

}