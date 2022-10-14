package com.example.library.model;

import lombok.*;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;


@Entity
@Table(name = "books")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	@ManyToMany(mappedBy = "books")
	List<Author> authors;
}
