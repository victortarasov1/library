package com.example.library.exception;

public class AuthorNotUniqueException extends RuntimeException{
    public AuthorNotUniqueException () {
        super("This author is already setted!");
    }
}
