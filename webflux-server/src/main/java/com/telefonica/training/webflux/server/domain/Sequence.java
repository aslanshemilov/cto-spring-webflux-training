package com.telefonica.training.webflux.server.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;

@Document("sequences")
@Data
@Accessors(chain=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sequence {
	
	@Id
	private String key;

	private Long counter;
	

}
