package com.example.library.exception;

public class AuthorDoesntContainsBookException extends RuntimeException{
    public AuthorDoesntContainsBookException() {
        super("author doesnt contains this book");
    }
}
