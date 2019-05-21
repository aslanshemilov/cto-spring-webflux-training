package com.telefonica.cdo.samples.spring.boot;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookPriceController {

    @Autowired
    private BookPriceRepository repository;

    @GetMapping(path = "/books/{isbn}", produces = "application/json")
    public Optional<BookPrice> get(@PathVariable String isbn) {
        return repository.findById(isbn);
    }

}
