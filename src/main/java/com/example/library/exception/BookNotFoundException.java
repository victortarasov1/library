package com.example.library.exception;

public class BookNotFoundException extends RuntimeException{
	public BookNotFoundException(Long id) {
        super("Book is not found, id="+id);
 }
}
