/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.telefonica.training.webflux.server.domain.Project;

import reactor.core.publisher.Mono;

@Repository
public class ProjectsRepository {

	private Map<Long, Project> projects;

	public ProjectsRepository() {
		projects = new HashMap<>();
	}

	public Mono<Void> save(Project project) {
		projects.put(project.getId(), project);
		return Mono.empty();
	}

	public Mono<Project> findById(long projectId) {
		Project project = projects.get(projectId);
		return (project == null) ? Mono.empty() : Mono.just(project);
	}

	public Mono<Void> deleteById(long projectId) {
		projects.remove(projectId);
		return Mono.empty();
	}

}
