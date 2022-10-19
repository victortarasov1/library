package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorContainsBookException;
import com.example.library.model.Book;

public interface BookService {
    void checkIfAnotherAuthorsHaveThisBook(Book b);

}
