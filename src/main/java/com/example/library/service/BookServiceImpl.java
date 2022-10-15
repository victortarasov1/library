package com.example.library.service;

import com.example.library.dto.BookDto;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class BookServiceImpl implements BookService{
    private BookRepository bookRepository;
    private ModelMapper mapper;
    @Override
    public void checkIfBookIsUnique(BookDto dto) {
        var books = bookRepository.findAll().stream().filter(book -> book.getAuthors().size() != 0).map(book -> mapper.map(book, BookDto.class)).toList();
        for (var book : books){
            if(book.equals(dto)){
                dto.setId(book.getId());
            }
        }
    }
}
