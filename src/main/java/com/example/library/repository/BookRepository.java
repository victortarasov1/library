package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select * from books join authors_books ab on books.id = ab.books_id join authors on ab.authors_id = authors.id and authors.actuality = 0 group by books.id", nativeQuery = true)
    List<Book> getBooks();

    @Query(value = "select * from books join authors_books ab on books.id = ab.books_id and books.id = ?1 join authors a on a.id = ab.authors_id and a.email = ?2 and a.actuality = 0", nativeQuery = true)
    Optional<Book> checkIfAuthorContainsBookById(Long id, String email);


    @EntityGraph(attributePaths = "authors", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Book> getBookAndAuthorsById(long id);
}
