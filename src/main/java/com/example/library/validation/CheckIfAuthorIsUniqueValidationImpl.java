package com.example.library.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.dto.AuthorFullDto;
import com.example.library.model.Author;
import com.example.library.service.AuthorsService;


public class CheckIfAuthorIsUniqueValidationImpl implements ConstraintValidator <CheckIfAuthorIsUniqueValidation, AuthorFullDto>{
	@Autowired
	AuthorsService authorService; 
	@Override
	public boolean isValid(AuthorFullDto value, ConstraintValidatorContext context) {
		try {
			Author author = authorService.findEquals(value.toAuthor());
			if (author.getId() == null || author.getId().equals(value.getId())) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
}
