package com.example.library.dto;

import javax.validation.constraints.NotNull;

import com.example.library.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookDto bookDto = (BookDto) o;
		return Objects.equals(title, bookDto.title) && Objects.equals(description, bookDto.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, description);
	}
}
