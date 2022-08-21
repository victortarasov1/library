package com.example.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	public AuthorServiceImpl(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	@Override
	public List<Author> findAll() {
		return authorRepository.findAll();
	}
	@Override
	public Author findById(Long id) {		
		return authorRepository.findById(id).orElseThrow( () -> new AuthorNotFoundException(id));
	}
	@Override
	public void changeAuthor(Author author) {
		
		Author changedAuthor = authorRepository.findById(author.getId()).orElseThrow( () -> new AuthorNotFoundException(author.getId()));
		changedAuthor.setAge(author.getAge());
		changedAuthor.setName(author.getName());
		changedAuthor.setSecondName(author.getSecondName());
		changedAuthor.getBooks().forEach(book -> author.deleteBook(book));
		changedAuthor.setBooks(author.getBooks());
		
		authorRepository.save(changedAuthor);

	}
	@Override
	public void deleteById(Long id) {
		try {
			authorRepository.deleteById(id);
		} catch (AuthorNotFoundException ex) {
			throw new AuthorNotFoundException(id);
		} 
		
	}
	@Override
	public Author save(Author author) {
		/*Author equalsAuthor = findEquals(author);
		authorRepository.save(equalsAuthor);*/
		authorRepository.save(author);
		//return authorRepository.findById(equalsAuthor.getId()).orElse(authorRepository.findAll().get(authorRepository.findAll().size()-1));
		return authorRepository.findById(author.getId()).orElse(authorRepository.findAll().get(authorRepository.findAll().size()-1));
	}
	@Override
	public Author addTitle(Long id, Book book) {
		Author author = findById(id);
		author.addBook(book);
		save(author);
		return author;
	}
	@Override
	public Author findEquals(Author author) {
		List <Author> authors = authorRepository.findAll();
		for(Author auth : authors ) {
			if(auth.getName().equals(author.getName()) && auth.getSecondName().equals(author.getSecondName())) {
				return auth;
			}
		}
		return author;
	}
	
	

}
