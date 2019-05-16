package com.telefonica.cdo.samples.spring.boot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_price")
public class BookPrice {

    @Column(name = "isbn")
    @Getter
    @Id
    @Setter
    private String isbn;

    @Column(name = "price")
    @Getter
    @Setter
    private Double price;

}
