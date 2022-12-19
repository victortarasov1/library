package com.example.library.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.library.dto.AuthorDto;
import com.example.library.exception.*;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.dto.AuthorFullDto;
import com.example.library.dto.BookDto;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryRestController {
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder encoder;
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.access_token.time}")
    private Integer ACCESS_TOKEN_TIME;

    @GetMapping
    public List<AuthorDto> findAll() {
        return authorRepository.findAllByActuality(Actuality.ACTIVE).stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }


    @PostMapping
    public AuthorDto addAuthor(@RequestBody @Valid AuthorFullDto dto) {
        if (authorRepository.findEqualsAuthors(dto.getEmail(), dto.getName(), dto.getSecondName(), 0L).isPresent()) {
            throw new AuthorNotUniqueException();
        }
        var author = dto.toAuthor();
        author.setPassword(encoder.encode(dto.getPassword()));
        return modelMapper.map(authorRepository.save(author), AuthorDto.class);
    }


    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookRepository.getBooksWithAuthors().stream().distinct().map(book -> modelMapper.map(book, BookDto.class)).toList();
    }

    @GetMapping("/books/{id}")
    public List<AuthorDto> getAuthors(@PathVariable Long id) {
        var authors = authorRepository.getAuthorsOfBook(id);
        if (authors == null) {
            throw new BookNotFoundException(id);
        }
        return authors.stream().map(author -> modelMapper.map(author, AuthorDto.class)).toList();
    }

    @GetMapping("/refresh")
    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                var instant = Instant.now();
                var refresh_token = authorizationHeader.substring("Bearer ".length());
                var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
                var verifier = JWT.require(algorithm).build();
                var decoderJWT = verifier.verify(refresh_token);
                var username = decoderJWT.getSubject();
                var user = authorRepository.findAuthorByEmailAndActuality(username, Actuality.ACTIVE)
                        .orElseThrow(() -> new AuthorNotFoundException(username));
                var access = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(instant.plus(ACCESS_TOKEN_TIME, ChronoUnit.MINUTES))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", List.of(user.getRole().name()))
                        .sign(algorithm);

                var tokens = new HashMap<String, String>();
                tokens.put("access_token", access);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception ex) {
                response.setHeader("error", ex.getMessage());
                response.setStatus(FORBIDDEN.value());
                var error = new HashMap<String, String>();
                error.put("error_message", ex.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
