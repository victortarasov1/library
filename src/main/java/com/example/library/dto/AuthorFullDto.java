package com.example.library.dto;

import com.example.library.enums.Role;
import com.example.library.model.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorFullDto {
    @Size(min = 1, max = 15, message = "first name must be setted")
    private String name;
    @Size(min = 1, max = 15, message = "second name must be setted")
    private String secondName;
    @Min(value = 1, message = "age must be bigger then zero")
    @Max(value = 99, message = "age must be lover then hundred")
    private int age;
    @NotNull(message = "password name mst be setted!")
    @Size(min = 2, max = 10)
    private String password;

    @NotNull(message = "email name mst be setted!")
    @Email
    private String email;

    public AuthorFullDto(Author author) {
        this.name = author.getName();
        this.secondName = author.getSecondName();
        this.age = author.getAge();
        this.password = author.getPassword();
        this.email = author.getEmail();
    }

    public Author toAuthor() {
        var author = new Author();
        author.setName(name);
        author.setSecondName(secondName);
        author.setAge(age);
        author.setEmail(email);
        author.setPassword(password);
        author.setRole(Role.ROLE_USER);
        return author;
    }

}
