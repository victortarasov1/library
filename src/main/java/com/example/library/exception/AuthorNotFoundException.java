package com.example.library.exception;

public class AuthorNotFoundException extends RuntimeException {
	 public AuthorNotFoundException(Long id) {
	        super("Author is not found, id="+id);
	    }
}
