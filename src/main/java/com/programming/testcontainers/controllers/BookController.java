package com.programming.testcontainers.controllers;

import com.programming.testcontainers.dtos.BookRequest;
import com.programming.testcontainers.dtos.BookResponse;
import com.programming.testcontainers.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody BookRequest bookRequest){
        bookService.create(bookRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> findAll(){
        return bookService.findAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse findById(@PathVariable UUID id){
        return bookService.findById(id);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse update(@PathVariable UUID id, @RequestBody BookRequest bookRequest){
        return bookService.update(bookRequest,id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id){
        bookService.deleteById(id);
    }
}
