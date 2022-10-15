package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.exception.AuthorContainsBookException;

public interface BookService {
    void checkIfAnotherAuthorsHaveThisBook(BookDto dto);
    void checkIfAuthorAlreadyContainsBook(Long authorId, BookDto dto) throws AuthorContainsBookException;
}
