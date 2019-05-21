package com.telefonica.cdo.samples.spring.boot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BookController {

    @Autowired
    private BookRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(path = "/books", produces = "application/json")
    public List<Book> list() {

        List<Book> books = repository.findAll();

        for (Book book : books) {

            String url = String.format("http://localhost:8089/books/%s", book.getIsbn());

            BookPrice bookPrice = restTemplate.getForEntity(url, BookPrice.class).getBody();

            book.setPrice(bookPrice.getPrice());

        }

        return books;

    }

}
