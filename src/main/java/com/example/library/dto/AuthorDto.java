package com.example.library.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.library.model.Author;
import com.example.library.model.Actuality;
import com.example.library.validation.CheckIfAuthorIsUniqueValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@CheckIfAuthorIsUniqueValidation
public class AuthorDto {
	private Long id;
	@Size(min = 1, max = 15, message = "first name must be setted")
	private String name;
	@Size(min = 1, max = 15, message = "second name must be setted")
	private String secondName;
	@Min(value = 1, message = "age must be bigger then zero")
	@Max(value = 99, message = "age must be lover then hundred")
	private int age;
	public Author toAuthor() {
		var author = new Author();
		author.setName(name);
		author.setSecondName(secondName);
		author.setAge(age);
		author.setActuality(Actuality.ACTIVE);
		return author;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AuthorDto authorDto = (AuthorDto) o;
		return Objects.equals(name, authorDto.name) && Objects.equals(secondName, authorDto.secondName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, secondName);
	}
}
