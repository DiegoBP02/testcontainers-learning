package com.programming.testcontainers.services;

import com.programming.testcontainers.dtos.BookRequest;
import com.programming.testcontainers.dtos.BookResponse;
import com.programming.testcontainers.entities.Book;
import com.programming.testcontainers.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void create(BookRequest bookRequest) {
        Book book = new Book(bookRequest.getName());
        bookRepository.save(book);
    }

    public BookResponse findById(UUID bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        return mapToBookResponse(book);
    }

    public List<BookResponse> findAll(){
        List<Book> bookList = bookRepository.findAll();
        return bookList.stream().map(this::mapToBookResponse).toList();
    }

    public BookResponse update(BookRequest bookRequest, UUID bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(EntityNotFoundException::new);
        updateData(book, bookRequest);
        bookRepository.save(book);
        return mapToBookResponse(book);
    }

    private void updateData(Book book, BookRequest bookRequest) {
        book.setName(bookRequest.getName());
    }


    public void deleteById(UUID bookId){
        bookRepository.deleteById(bookId);
    }

    private BookResponse mapToBookResponse(Book book) {
        return BookResponse.builder()
                .name(book.getName())
                .build();
    }
}
