package com.example.library.dto;

import javax.validation.constraints.NotNull;

import com.example.library.model.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthorDto {
	private Long id;
	@NotNull(message = "name must be setted")
	private String name;
	@NotNull(message = "second name must be setted")
	private String secondName;
	private int age;
	public Author toAuthor() {
		Author author = new Author();
		author.setName(name);
		author.setSecondName(secondName);
		author.setId(id);
		return author;
	}

}
