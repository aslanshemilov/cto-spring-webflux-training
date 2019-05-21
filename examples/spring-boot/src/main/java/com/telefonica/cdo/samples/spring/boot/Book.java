package com.telefonica.cdo.samples.spring.boot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")
public class Book {

    @Column(name = "id")
    @Getter
    @Id
    @Setter
    private Long id;

    @Column(name = "title")
    @Getter
    @Setter
    private String title;

    @Column(name = "isbn")
    @Getter
    @Setter
    private String isbn;

    @Getter
    @Setter
    @Transient
    private Double price;

}
