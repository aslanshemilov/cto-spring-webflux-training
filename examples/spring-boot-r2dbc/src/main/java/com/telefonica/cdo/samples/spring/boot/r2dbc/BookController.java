package com.telefonica.cdo.samples.spring.boot.r2dbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping(path = "/books", produces = "application/json")
    public Flux<Book> list() {
        return repository.findAll();
    }

}
