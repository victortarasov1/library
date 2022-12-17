package com.example.library.repository;


import com.example.library.model.Actuality;
import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.model.Author;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
    Optional<Author> findByEmail(String email);
    List<Author> findAllByActuality(Actuality actuality);
    Optional<Author> findAuthorByIdAndActuality(Long id, Actuality actuality);
    Optional<Author> findAuthorByEmailAndActuality(String email, Actuality actuality);
    @Query(value = "select * from authors join authors_books ab on authors.id = ab.authors_id and authors.actuality = 0 and ab.books_id = ?1", nativeQuery = true)
    List<Author> getAuthorsOfBook(Long bookId);

    @Query(value = "select a from Author a where a.email = ?1 and a.name = ?2 and a.secondName = ?3 and a.id <> ?4")
    Optional<Author> findEqualsAuthors(String email, String name, String secondName, Long id);
}

