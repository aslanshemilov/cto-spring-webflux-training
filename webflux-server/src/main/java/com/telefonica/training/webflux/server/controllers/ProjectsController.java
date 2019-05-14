/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.controllers;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.services.ProjectsService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin
public class ProjectsController {

	private final ProjectsService projectsService;

	public ProjectsController(ProjectsService projectsService) {
		this.projectsService = projectsService;
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<ResponseEntity<Project>> createProject(@RequestBody Project project) {
		return projectsService.createProject(project)
				.map(p -> {
					URI locationUri = new UriTemplate("/api/projects/{projectId}").expand(p.getId());
					return ResponseEntity.created(locationUri).body(p);
				});
	}

	@GetMapping
	public Flux<Project> findProjects() {
		return projectsService.findProjects();
	}

	@GetMapping("/{projectId}")
	public Mono<Project> getProject(@PathVariable long projectId) {
		return projectsService.getProject(projectId);
	}

	@PutMapping("/{projectId}")
	public Mono<Project> updateProject(@PathVariable long projectId, @RequestBody Project project) {
		project.setId(projectId);
		return projectsService.updateProject(project);
	}

	@DeleteMapping("/{projectId}")
	public Mono<ResponseEntity<Void>> deleteProject(@PathVariable long projectId) {
		return projectsService.deleteProject(projectId)
				.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}
}
