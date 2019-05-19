/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.services.ProjectsService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController {

	private final ProjectsService projectsService;

	public ProjectsController(ProjectsService projectsService) {
		this.projectsService = projectsService;
	}

	@GetMapping("{projectId}")
	public Mono<Project> getProject(@PathVariable long projectId) {
		return projectsService.getProject(projectId);
	}

}
