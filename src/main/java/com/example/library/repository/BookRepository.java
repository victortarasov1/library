package com.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository <Book, Long> {
    @Query(value = "select * from books join authors_books ab on ab.authors_id = ?1 and ab.books_id = ?2 join authors on ab.authors_id = authors.id and authors.actuality = 0", nativeQuery = true)
    Optional<Book> checkIfAuthorContainsBookById(Long authorId, Long id);
    @Query(value = "select * from books join authors_books ab on books.id = ab.books_id and ab.authors_id = ?1", nativeQuery = true)
    List<Book> getBooksByAuthorId(Long id);

    @Query(value= "select * from books join authors_books ab on books.id = ab.books_id join authors on ab.authors_id = authors.id and authors.actuality = 0", nativeQuery = true)
    List<Book> getBooksWithAuthors();



}
