package com.telefonica.cdo.samples.spring.boot.r2dbc;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table("book")
public class Book {

    @Getter
    @Id
    @Setter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String isbn;

    @Getter
    @Setter
    private Double price;

}
