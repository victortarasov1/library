package com.example.library.model;


import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
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
    private Actuality actuality;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;

    public void addBook(Book book) {
        if(books != null) {
            var tmp = books;
            books = new LinkedList<>();
            books.addAll(tmp);
            books.add(book);
        } else {
            books = new LinkedList<>();
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        if(books != null){
            books.remove(book);
        }
    }
}
