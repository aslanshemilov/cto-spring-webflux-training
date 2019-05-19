/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.services;

import org.springframework.stereotype.Service;

import com.telefonica.training.webflux.server.domain.RepoReport;
import com.telefonica.training.webflux.server.exceptions.NotFoundException;
import com.telefonica.training.webflux.server.github.GithubClient;

import reactor.core.publisher.Mono;

@Service
public class ReposService {

	private final ProjectsService projectsService;
	private final GithubClient githubClient;
	
	public ReposService(ProjectsService projectsService, GithubClient githubClient) {
		this.projectsService = projectsService;
		this.githubClient = githubClient;
	}

	public Mono<RepoReport> getRepoReport(long projectId, String repo) {
		return projectsService.getProject(projectId)
				.map(project -> {
					if (project.getRepositories() == null || !project.getRepositories().contains(repo)) {
						throw new NotFoundException();
					}
					return project;
				})
				.flatMap(project -> githubClient.getRepoReport(project, repo));
	}

}
