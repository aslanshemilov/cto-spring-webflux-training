/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.controllers;

import java.util.Arrays;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telefonica.training.webflux.server.domain.Project;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController {

	@GetMapping("{projectId}")
	public Mono<Project> getProject(@PathVariable long projectId) {
		Project fakeProject = new Project()
				.setId(projectId)
				.setName("demo")
				.setGithubToken("github-token")
				.setOrganization("demo-organization")
				.setRepositories(Arrays.asList("demo-repo-1", "demo-repo-2"));
		return Mono.just(fakeProject);
	}

}
