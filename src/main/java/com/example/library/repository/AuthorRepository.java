package com.example.library.repository;


import com.example.library.model.Actuality;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.model.Author;

import java.util.List;
import java.util.Optional;

//@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
	//Optional <Author> getEquals(String name, String secondName);
    List<Author> findAllByActuality(Actuality actuality);
    Optional<Author> findAuthorByIdAndActuality(Long id, Actuality actuality);
}
