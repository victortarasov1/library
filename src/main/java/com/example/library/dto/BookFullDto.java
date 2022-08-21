package com.example.library.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.library.model.Book;


/*
 * full info about book
 */

public class BookFullDto extends BookDto {
	@Size(min = 10, max = 30)
	private String description;
	@Valid
	private AuthorDto author;
	@Override
	public Book toBook() {
		Book book = new Book();
		//book.setId(id);
		book.setId(getId());
		book.setDescription(description);
		//book.setTitle(title);
		book.setTitle(getTitle());
		book.setAuthor(author.toAuthor());
		return book;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public AuthorDto getAuthor() {
		return author;
	}
	public void setAuthor(AuthorDto author) {
		this.author = author;
	}
	/*public BookFullDto(@NotNull(message = "title must be setted!") String title) {
		super( title);
	}
	public BookFullDto() {
		super();
	}*/
	
}
