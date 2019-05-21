/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.github;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.telefonica.training.webflux.server.configuration.Config;
import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.domain.RepoReport;
import com.telefonica.training.webflux.server.domain.RequestContext;
import com.telefonica.training.webflux.server.headers.CustomHeaders;

import reactor.core.publisher.Mono;

@Component
public class GithubClient {

	private final WebClient webClient;

	public GithubClient(Config config) {
		webClient = WebClient.builder()
				.baseUrl(config.getGithub().getUrl())
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.filter(addCorrelatorHeader())
				.build();
	}

	public Mono<RepoReport> getRepoReport(Project project, String repo) {
		return webClient.get()
			.uri("/repos/{organization}/{repository}/languages", project.getOrganization(), repo)
			.headers(headers -> {
				if (project.getGithubToken() != null) {
					headers.add(HttpHeaders.AUTHORIZATION, "token " + project.getGithubToken());
				}
			})
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Map<String, Integer>> (){})
			.map(languages -> new RepoReport().setLanguages(languages));
	}

	private static ExchangeFilterFunction addCorrelatorHeader() {
		return ExchangeFilterFunction.ofRequestProcessor(request ->
			Mono.subscriberContext()
					.map(ctxt -> ctxt.getOrDefault(RequestContext.class, new RequestContext()))
					.map(requestContext -> ClientRequest.from(request).header(CustomHeaders.CORRELATOR, requestContext.getCorrelator()).build())
		);
	}

}
