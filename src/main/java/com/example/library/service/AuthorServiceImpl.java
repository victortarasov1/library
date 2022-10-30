package com.example.library.service;

import com.example.library.exception.AuthorContainsBookException;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    private void checkIfAnotherAuthorsHaveThisBook(Book b) {
        var books = bookRepository.findAll().stream().filter(book -> book.getAuthors().size() != 0).toList();
        for (var book : books){
            if(book.equals(b)){
                b.setId(book.getId());
            }
        }
    }

    @Override
    public List<Book> addBook(Author author, Book book) {
        if(author.getBooks().contains(book)){
            throw new AuthorContainsBookException();
        }
        checkIfAnotherAuthorsHaveThisBook(book);
        author.addBook(book);
        return authorRepository.save(author).getBooks();
    }

}
