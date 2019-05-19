/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.services;

import org.springframework.stereotype.Service;

import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.repositories.ProjectsRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectsService {

	private ProjectsRepository projectsRepository;

	public ProjectsService(ProjectsRepository projectsRepository) {
		this.projectsRepository = projectsRepository;
	}

	public Mono<Project> createProject(Project project) {
		return projectsRepository.next()
				.map(projectId -> project.setId(projectId))
				.flatMap(p -> projectsRepository.save(project));
	}

	public Flux<Project> findProjects() {
		return projectsRepository.findAll();
	}

	public Mono<Project> getProject(long projectId) {
		return projectsRepository.findById(projectId);
	}

	public Mono<Project> updateProject(Project project) {
		return projectsRepository.save(project);
	}

	public Mono<Void> deleteProject(long projectId) {
		return projectsRepository.deleteById(projectId);
	}
}
