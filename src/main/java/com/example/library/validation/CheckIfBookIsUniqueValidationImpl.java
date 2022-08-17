package com.example.library.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.library.dto.BookDto;
import com.example.library.model.Book;
import com.example.library.service.BookService;
@Component
public class CheckIfBookIsUniqueValidationImpl implements ConstraintValidator <CheckIfBookIsUniqueValidation, BookDto > {
	@Autowired
	BookService bookService;
	@Override
	public boolean isValid(BookDto value, ConstraintValidatorContext context) {
		try {
			/*if(value.getId() == null) {
				
				Book book = bookService.findEquals(value.toBook());
				System.out.println(book.getTitle());
				if(book.getId() == null) {
					return true;
				} else {
					return false;
				}
				
			}
			return true;*/
			Book book = bookService.findEquals(value.toBook());
			if (book.getId() == null || book.getId().equals(value.getId())) {
				return true;
			} else {
				return false;
			}
		} catch( Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
