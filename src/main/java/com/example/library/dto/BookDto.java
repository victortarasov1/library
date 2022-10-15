package com.example.library.dto;

import javax.validation.constraints.NotNull;

import com.example.library.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * book only with title
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
	private Long id;
	@NotNull(message  = "title must be setted!")
	private String title;
	private String description;
	public Book toBook() {
		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		return book;
	}

}
