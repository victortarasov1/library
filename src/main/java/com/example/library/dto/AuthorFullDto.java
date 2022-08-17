package com.example.library.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.Valid;
import javax.validation.constraints.Max;

import com.example.library.model.Author;
import com.example.library.validation.CheckIfAuthorIsUniqueValidation;

/*
 * all info about author
 */
@CheckIfAuthorIsUniqueValidation
public class AuthorFullDto extends AuthorDto {
	@Min(value = 0, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")
	private int age;
	@Valid
	private List <BookDto> books = new ArrayList <>();
	@Override
	public Author toAuthor () {
		Author author = new Author ();
		author.setAge(age);
		author.setBooks(books.stream().map(book -> book.toBook()).collect(Collectors.toList()));
		author.setId(getId());
		author.setName(getName());
		author.setSecondName(getSecondName());
		return author;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List <BookDto> getBooks() {
		return books;
	}
	public void setBooks(List <BookDto> books) {
		this.books = books;
	}
}
