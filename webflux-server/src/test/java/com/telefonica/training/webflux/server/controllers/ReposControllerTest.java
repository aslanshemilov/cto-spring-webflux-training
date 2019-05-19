/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.controllers;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.telefonica.training.webflux.server.domain.RepoReport;
import com.telefonica.training.webflux.server.exceptions.NotFoundException;
import com.telefonica.training.webflux.server.services.ReposService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout="45000")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReposControllerTest {
	@Autowired
	private WebTestClient webClient;

	@MockBean
	private ReposService reposService;

	@After
	public void reset() {
		Mockito.reset(reposService);
	}

	@Test
	public void shouldGetRepoReport() {
		Map<String, Integer> languages = new HashMap<>();
		languages.put("JavaScript", 1000);
		languages.put("Go", 10000);
		RepoReport repoReport = new RepoReport()
				.setLanguages(languages);
		Mockito.when(reposService.getRepoReport(1L, "repo")).thenReturn(Mono.just(repoReport));

		webClient.get()
			.uri("/api/projects/1/repos/repo")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody(RepoReport.class).isEqualTo(repoReport);
	}

	@Test
	public void shouldNotGetRepoReportByNotFound() {
		Mockito.when(reposService.getRepoReport(1L, "repo")).thenReturn(Mono.error(new NotFoundException()));

		webClient.get()
			.uri("/api/projects/1/repos/repo")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isNotFound();

	}

}