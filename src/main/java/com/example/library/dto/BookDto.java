package com.example.library.dto;

import javax.validation.constraints.NotNull;

import com.example.library.model.Book;
import com.example.library.validation.CheckIfBookIsUniqueValidation;

/*
 * book only with title
 */
@CheckIfBookIsUniqueValidation
public class BookDto {
	private Long id;
	@NotNull(message  = "title must be setted!")
	private String title;
	public Book toBook() {
		Book book = new Book();
		book.setId(id);
		book.setTitle(title);
		return book;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
