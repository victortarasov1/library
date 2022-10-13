package com.example.library.repository;


import com.example.library.model.AuthorActuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.model.Author;

import java.util.List;
import java.util.Optional;

//@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
	//Optional <Author> getEquals(String name, String secondName);
    List<Author> findAllByAuthorActuality(AuthorActuality actuality);
    Optional<Author> findAuthorByIdAndAuthorActuality(Long id, AuthorActuality actuality);
}
