package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorContainsBookException;
import com.example.library.model.Author;
import com.example.library.model.Book;

import java.util.List;

public interface BookService {
    void checkIfAnotherAuthorsHaveThisBook(Book b);
    List <Book> addBook(Author author, Book books);
}
