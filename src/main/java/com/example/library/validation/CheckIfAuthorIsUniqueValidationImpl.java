package com.example.library.validation;

import com.example.library.dto.AuthorDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CheckIfAuthorIsUniqueValidationImpl implements ConstraintValidator <CheckIfAuthorIsUniqueValidation, AuthorDto>{
	@Override
	public boolean isValid(AuthorDto value, ConstraintValidatorContext context) {
		try {
			return true;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
}
