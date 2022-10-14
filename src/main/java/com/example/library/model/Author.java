package com.example.library.model;


import com.example.library.dto.BookDto;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "authors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "second_name")
    private String secondName;
    private int age;
    private AuthorActuality authorActuality;
    /*
     * "cascade = CascadeType.ALL" - if removes author, his books will be removed too
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Book> books;

    public void addBook(Book book) {
        if (books == null){
            books = new ArrayList<>();
        }
        books.add(book);
    }
}
