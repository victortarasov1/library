package com.example.library.exception;

public class AuthorContainsBookException extends RuntimeException {
    public   AuthorContainsBookException(){
        super("this author already contains this book");
    }
}
