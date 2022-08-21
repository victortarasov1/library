package com.example.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorService authorService;
	public BookServiceImpl(BookRepository bookRepository, AuthorService authorService) {
		this.bookRepository = bookRepository;
		this.authorService = authorService;
	}
	@Override
	public List<Book> findAll() {	
		return bookRepository.findAll();
	}
	@Override
	public Book findById(Long id) {
		
		return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
	}
	@Override
	public void deleteById(Long id) {
		try {
			Book book = findById(id);
			book.setAuthor(null);
			bookRepository.save(book);
			bookRepository.deleteById(id);
		} catch ( Exception ex) {
			throw new BookNotFoundException(id);
		}
		
	}
	@Override
	public Book save(Book book) {
		Author author = authorService.findEquals(book.getAuthor());
		book.setAuthor(author);
		bookRepository.save(book);
		return findAll().get(findAll().size() -1);
	}
	@Override
	public void changeBook(Book book) {
		Book changedBook = findById(book.getId());
		changedBook.setDescription(book.getDescription());
		changedBook.setTitle(book.getTitle());
		if(!changedBook.getAuthor().getName().equals(book.getAuthor().getName()) || !changedBook.getAuthor().getSecondName().equals(book.getAuthor().getSecondName())) {
			Author author = authorService.findEquals(book.getAuthor());
			changedBook.setAuthor(author);
		}
		bookRepository.save(changedBook);
		
	}
	/*@Override
	public void changeAuthor(Long id, Author author) {
		Book book = findById(id);
		book.setAuthor(author);
		save(book);
	}*/
	@Override
	public Book findEquals(Book book) {
		List <Book> books = bookRepository.findAll();
		for (Book b : books) {
			if(b.getTitle().equals(book.getTitle())) {
				return b;
			}
		}
		return book;
	}

}
