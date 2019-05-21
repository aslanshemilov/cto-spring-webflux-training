/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.telefonica.training.webflux.server.domain.PageView;
import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.exceptions.NotFoundException;
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
		return projectsRepository.next(Project.class)
				.map(projectId -> project.setId(projectId))
				.flatMap(p -> projectsRepository.save(project));
		
//		return projectsRepository.save(project.setId(1L));
	}

	public Flux<Project> findProjects() {
		return projectsRepository.findAll();
	}

	public Mono<Project> getProject(long projectId) {
		return projectsRepository.findById(projectId)
				.switchIfEmpty(Mono.error(new NotFoundException()));
	}

	public Mono<Project> updateProject(Project project) {
		return getProject(project.getId())
				.flatMap(oldProject -> projectsRepository.save(project));
	}

	public Mono<Void> deleteProject(long projectId) {
		return projectsRepository.deleteById(projectId);
	}

	public Mono<PageView> findProjectsPaginated(int page, int size) {
		// contador total documentos
		// coger los datos de la pagina
		
		return Mono.zip(projectsRepository.count(),
				projectsRepository.getProjectsPaginated(PageRequest.of(page, size)).collectList()
				).map(tupleResult -> new PageView().setPage(page)
						.setProjects(tupleResult.getT2())
						.setSize(size)
						.setTotal(tupleResult.getT1())
						);
	}
}
