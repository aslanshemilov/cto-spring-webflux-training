/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.domain;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class Project {
	private Long id;
	private String name;
	private String organization;
	private String githubToken;
	private List<String> repositories;
}
