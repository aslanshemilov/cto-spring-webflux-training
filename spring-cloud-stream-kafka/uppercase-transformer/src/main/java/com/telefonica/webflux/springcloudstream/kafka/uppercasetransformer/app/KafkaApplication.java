package com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = { "com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer" })
public class KafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}
}
