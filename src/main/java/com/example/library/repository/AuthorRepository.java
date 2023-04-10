package com.example.library.repository;


import com.example.library.model.Actuality;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.model.Author;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    Optional<Author> findByEmail(String email);
    List<Author> findAll(Actuality actuality);
    @Query(value = "select * from authors join authors_books ab on authors.id = ab.authors_id and authors.actuality = 0 and ab.books_id = ?1", nativeQuery = true)
    List<Author> getAuthorsOfBook(Long bookId);
    Optional<Author> findAuthorByEmailAndNameAndSecondNameAndIdNot(String email, String name, String secondName, Long id);

    @Query(value = "select * from books join authors_books ab on books.id = ab.books_id and books.id = ?2 join authors a on a.id = ab.authors_id and a.email != ?1 and a.actuality = 0", nativeQuery = true)
    List<Author> getAnotherBookAuthors(String email, Long bookId);
}

