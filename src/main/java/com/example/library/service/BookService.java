package com.example.library.service;

import com.example.library.dto.BookDto;

public interface BookService {
    void checkIfAnotherAuthorsHaveThisBook(BookDto dto);
}
