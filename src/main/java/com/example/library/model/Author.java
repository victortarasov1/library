package com.example.library.model;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

}
