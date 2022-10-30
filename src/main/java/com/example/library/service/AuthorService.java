package com.example.library.service;

import com.example.library.model.Author;
import com.example.library.model.Book;

import java.util.List;

public interface AuthorService {
    List <Book> addBook(Author author, Book books);
}
