package com.example.library.service;

import java.util.List;

import com.example.library.model.Author;
import com.example.library.model.Book;

public interface AuthorsService {
	public List<Author> findAll();
	public Author findById(Long id);
	public void changeAuthor(Author author);
	public void deleteById(Long id);
	public Author save(Author author);
	public Author addTitle(Long id,Book book);
	public Author findEquals(Author author);
	//public Author findEquals(String name, String secondName);
}
