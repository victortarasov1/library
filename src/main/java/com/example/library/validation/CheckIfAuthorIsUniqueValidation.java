package com.example.library.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckIfAuthorIsUniqueValidationImpl.class)
public @interface CheckIfAuthorIsUniqueValidation {
	 String message() default "This author is already setted!";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}
