package com.example.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.library.model.Book;
@Service
public interface BookService {
	public List <Book> findAll();
	public Book findById(Long id);
	public void deleteById(Long id);
	public Book save(Book book);
	public void changeBook(Book book);
	//public void changeAuthor(Long id,Author author);
	//public void findEquals()
	Book findEquals(Book book);
}
