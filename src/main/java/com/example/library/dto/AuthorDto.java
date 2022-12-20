package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

}
