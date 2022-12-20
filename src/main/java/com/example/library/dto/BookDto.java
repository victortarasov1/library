package com.example.library.dto;


import javax.validation.constraints.Size;

import com.example.library.model.Book;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookDto {
	private Long id;
	@Size(min = 1, max = 30, message ="title must be between 1 and 30 literals")
	private String title;
	@Size(min = 10, max = 100, message ="description must be between 10 and 100 literals")
	private String description;
	public Book toBook() {
		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		book.setId(id);
		return book;
	}
}
