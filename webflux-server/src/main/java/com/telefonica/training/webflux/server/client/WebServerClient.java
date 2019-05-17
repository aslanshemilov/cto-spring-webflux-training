/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.telefonica.training.webflux.server.domain.RepoReport;

import reactor.core.publisher.Mono;

@Component
public class WebServerClient {

	private final WebClient webClient;

	public WebServerClient(@Value("${webflux-server.server.url}") String url, @Value("${server.port}") int port) {
		webClient = WebClient.builder()
				.baseUrl(url + ":" + port)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	public Mono<RepoReport> getRepoReport(Long projectId, String repo) {
		return webClient.get()
			.uri("/api/projects/{projectId}/repos/{repo}", projectId, repo)
			.retrieve()
			.bodyToMono(RepoReport.class);
	}

}
