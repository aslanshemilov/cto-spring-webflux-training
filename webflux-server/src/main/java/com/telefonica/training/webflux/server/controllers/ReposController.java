/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.telefonica.training.webflux.server.domain.RepoReport;
import com.telefonica.training.webflux.server.services.ReposService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects/{projectId}/repos")
public class ReposController {

	private final ReposService reposService;

	public ReposController(ReposService reposService) {
		this.reposService = reposService;
	}

	@GetMapping("/{repo}")
	public Mono<RepoReport> getRepoReport(@PathVariable Long projectId, @PathVariable String repo) {
		return reposService.getRepoReport(projectId, repo);
	}

}
