package com.example.library.repository;


import com.example.library.model.Actuality;
import com.example.library.model.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByEmail(String email);

    List<Author> findAll(Actuality actuality);

    Optional<Author> findAuthorByEmailAndNameAndSecondNameAndIdNot(String email, String name, String secondName, Long id);

    @EntityGraph(attributePaths = "books", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Author> findAuthorAndBooksByEmail(String email);
}

