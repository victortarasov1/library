package com.example.library.dto;

import com.example.library.model.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    @Size(min = 1, max = 15, message = "first name must be setted")
    private String name;
    @Size(min = 1, max = 15, message = "second name must be setted")
    private String secondName;

    public AuthorDto(Author author) {
        this.name = author.getName();
        this.secondName = author.getSecondName();
    }
}
