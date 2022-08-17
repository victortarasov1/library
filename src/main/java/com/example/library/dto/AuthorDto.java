package com.example.library.dto;

import javax.validation.constraints.NotNull;

import com.example.library.model.Author;

/*
 * Author without  age and list of books
 */
public class AuthorDto {
	private Long id;
	@NotNull(message = "name must be setted")
	private String name;
	@NotNull(message = "second name must be setted")
	private String secondName;
	public Author toAuthor() {
		Author author = new Author();
		author.setName(name);
		author.setSecondName(secondName);
		author.setId(id);
		return author;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
