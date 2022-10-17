package com.example.library.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.library.model.Book;
import lombok.*;

import java.util.Objects;

/*
 * book only with title
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookDto {
	private Long id;
	@NotNull(message  = "title must be setted!")
	private String title;
	@Size(min = 0, max = 30, message ="description must be between 0 and 30 literals")
	private String description;
	public Book toBook() {
		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		return book;
	}
}
