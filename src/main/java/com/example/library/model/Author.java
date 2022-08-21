package com.example.library.model;


import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(name = "second_name")
	private String secondName;
	private int age;
	/*
	 * "cascade = CascadeType.ALL" - if removes author, his books will be removed too
	 */
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, /*orphanRemoval = true,*/fetch = FetchType.LAZY )
	private List <Book> books;
	
	@Override
	public int hashCode() {
		return Objects.hash(age, books, id, name, secondName);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return age == other.age && Objects.equals(books, other.books) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(secondName, other.secondName);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void deleteBook(Book book) {
		books.remove(book);
		book.setAuthor(null);
	}
	public void setBooks(List<Book> books) {
		books.forEach(book -> book.setAuthor(this));	
		this.books = books;
	}
	public void addBook(Book book) {
		book.setAuthor(this);
		books.add(book);
	}
	
}
