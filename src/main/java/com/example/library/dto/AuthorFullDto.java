package com.example.library.dto;

import javax.validation.constraints.*;

import com.example.library.model.Author;
import com.example.library.model.Actuality;
import com.example.library.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthorFullDto {
	@Size(min = 1, max = 15, message = "first name must be setted")
	private String name;
	@Size(min = 1, max = 15, message = "second name must be setted")
	private String secondName;
	@Min(value = 1, message = "age must be bigger then zero")
	@Max(value = 99, message = "age must be lover then hundred")
	private int age;
	@NotNull(message = "password name mst be setted!")
	@Size(min = 2, max = 10)
	private String password;

	@NotNull(message = "email name mst be setted!")
	@Email
	private String email;
	public Author toAuthor() {
		var author = new Author();
		author.setName(name);
		author.setSecondName(secondName);
		author.setAge(age);
		author.setEmail(email);
		author.setPassword(password);
		author.setRole(Role.ROLE_USER);
		author.setActuality(Actuality.ACTIVE);
		return author;
	}

}
