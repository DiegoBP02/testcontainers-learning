package com.programming.testcontainers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.testcontainers.dtos.BookRequest;
import com.programming.testcontainers.entities.Book;
import com.programming.testcontainers.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.programming.testcontainers.controllers.DatabasePostgresqlContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = DatabasePostgresqlContainer.getInstance();

    @BeforeEach
    void tearDown(){
        bookRepository.deleteAll();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;

    Book book = new Book("book");

    @Test
    void shouldCreateBook() throws Exception {
        BookRequest bookRequest = new BookRequest("name");
        String bookRequestString = objectMapper.writeValueAsString(bookRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestString))
                .andExpect(status().isCreated());
        assertEquals (1, bookRepository.findAll().size());
    }

    @Test
    void shouldReturnAllBooks() throws Exception{
        insertBook();
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(book.getName()));
    }

    @Test
    void shouldReturnABooks() throws Exception{
        insertBook();
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(book.getName()));
    }

    @Test
    void shouldUpdateABook() throws Exception{
        insertBook();
        BookRequest updateData = new BookRequest("random");
        String updateDataString = objectMapper.writeValueAsString(updateData);
        mockMvc.perform(MockMvcRequestBuilders.patch("/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateDataString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateData.getName()));
    }

    @Test
    void shouldDeleteABook() throws Exception{
        insertBook();
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + book.getId()))
                .andExpect(status().isNoContent());
        assertEquals(0, bookRepository.findAll().size());
    }

    private void insertBook() {
        bookRepository.save(book);
    }
}