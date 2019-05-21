package com.telefonica.cdo.samples.spring.boot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPriceRepository extends JpaRepository<BookPrice, String> {

}
