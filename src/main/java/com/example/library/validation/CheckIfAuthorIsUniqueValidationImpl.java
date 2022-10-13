package com.example.library.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.dto.AuthorFullDto;
import com.example.library.model.Author;


public class CheckIfAuthorIsUniqueValidationImpl implements ConstraintValidator <CheckIfAuthorIsUniqueValidation, AuthorFullDto>{
	@Override
	public boolean isValid(AuthorFullDto value, ConstraintValidatorContext context) {
		try {
			return true;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
}
