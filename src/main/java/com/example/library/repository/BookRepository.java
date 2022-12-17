package com.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository <Book, Long> {
    @Query(value = "select * from books join authors_books ab on ab.books_id = books.id and books.title = ?3 and books.id != ?2 join authors on ab.authors_id = authors.id and authors.email = ?1 and authors.actuality = 0", nativeQuery = true)
    List<Book> checkIdAuthorContainsBooksWithSameTitle(String email, Long id, String title);
    @Query(value = "select * from books join authors_books ab on books.id = ab.books_id join authors on ab.authors_id = authors.id and authors.email = ?1 and authors.actuality = 0", nativeQuery = true)
    List<Book> getBooksByAuthorEmail(String email);

    @Query(value= "select * from books join authors_books ab on books.id = ab.books_id join authors on ab.authors_id = authors.id and authors.actuality = 0", nativeQuery = true)
    List<Book> getBooksWithAuthors();

    @Query(value = "select * from books join authors_books ab on books.id = ab.books_id and books.id = ?1 join authors a on a.id = ab.authors_id and a.email = ?2 and a.actuality = 0", nativeQuery = true)
    Optional<Book> checkIfAuthorContainsBookById(Long id, String email);


}
