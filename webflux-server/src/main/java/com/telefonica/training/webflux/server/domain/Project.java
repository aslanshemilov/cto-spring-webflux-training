/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.domain;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;

@Document("projects")
@Data
@Accessors(chain=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {
	
	@Id
	private Long id;

	@NotEmpty(message = "name is mandatory")
	private String name;

	@NotEmpty(message = "organization is mandatory")
	@Pattern(regexp = "^[A-Za-z0-9-_]+$", message = "organization has an invalid value")
	private String organization;

	private String githubToken;

	@NotNull(message = "repositories is mandatory")
	private List<String> repositories;
}
