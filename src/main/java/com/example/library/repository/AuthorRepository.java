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
    List<Author> findAllByActuality(Actuality actuality);
    Optional<Author> findAuthorByIdAndActuality(Long id, Actuality actuality);

    @Query(value = "select * from authors join authors_books ab on authors.id = ab.authors_id and authors.actuality = 0 and ab.books_id = ?1", nativeQuery = true)
    List<Author> getAuthorsOfBook(Long bookId);
}
