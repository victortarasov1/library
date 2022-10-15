package com.example.library.dto;

import javax.validation.constraints.NotNull;

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
	@NotNull(message = "name must be setted")
	private String name;
	@NotNull(message = "second name must be setted")
	private String secondName;
	private int age;
	public Author toAuthor() {
		var author = new Author();
		author.setName(name);
		author.setSecondName(secondName);
		author.setId(id);
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
