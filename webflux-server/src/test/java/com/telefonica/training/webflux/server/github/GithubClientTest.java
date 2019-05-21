/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.github;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.telefonica.training.webflux.server.configuration.Config;
import com.telefonica.training.webflux.server.configuration.Config.Github;
import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.domain.RepoReport;
import com.telefonica.training.webflux.server.domain.RequestContext;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

public class GithubClientTest {

	private ClientAndServer mockServer;

	@Before
	public void startServer() {
		mockServer = ClientAndServer.startClientAndServer(1081);
	}

	@After
	public void stopServer() {
		mockServer.stop();
	}

	@Test
	public void shouldGetRepoReportWithoutToken() {
		mockServer.when(HttpRequest.request().withMethod("GET").withPath("/repos/testOrg/testRepo/languages")
						.withHeader("Unica-Correlator", "testCorr"))
				.respond(HttpResponse.response().withStatusCode(200).withHeader("Content-Type", "application/json")
						.withBody("{\"Python\": 10000, \"Java\": 5000}"));

		Config config = new Config().setGithub(new Github().setUrl("http://localhost:1081"));
		GithubClient client = new GithubClient(config);
		Project project = new Project().setOrganization("testOrg");
		RepoReport repoReport = client.getRepoReport(project, "testRepo")
				.subscriberContext(Context.of(RequestContext.class, new RequestContext().setCorrelator("testCorr")))
				.block();
		Assert.assertEquals(new Integer(10000), repoReport.getLanguages().get("Python"));
		Assert.assertEquals(new Integer(5000), repoReport.getLanguages().get("Java"));
	}

	@Test
	public void shouldGetRepoReportWithToken() {
		mockServer.when(HttpRequest.request().withMethod("GET").withPath("/repos/testOrg/testRepo/languages")
						.withHeader("Unica-Correlator", "testCorr").withHeader("Authorization", "token testToken"))
				.respond(HttpResponse.response().withStatusCode(200).withHeader("Content-Type", "application/json")
						.withBody("{\"Python\": 10000, \"Java\": 5000}"));

		Config config = new Config().setGithub(new Github().setUrl("http://localhost:1081"));
		GithubClient client = new GithubClient(config);
		Project project = new Project().setOrganization("testOrg").setGithubToken("testToken");
		Mono<RepoReport> repoReport = client.getRepoReport(project, "testRepo")
				.subscriberContext(Context.of(RequestContext.class, new RequestContext().setCorrelator("testCorr")));

		Map<String, Integer> languages = new HashMap<>();
		languages.put("Python", 10000);
		languages.put("Java", 5000);
		RepoReport expectedProjectReport = new RepoReport()
				.setLanguages(languages);
		StepVerifier.create(repoReport)
				.expectNext(expectedProjectReport)
				.verifyComplete();
	}

	@Test
	public void shouldNotGetRepoReportByNotFound() {
		mockServer.when(HttpRequest.request().withMethod("GET").withPath("/repos/testOrg/testRepo/languages")
						.withHeader("Unica-Correlator", "testCorr"))
				.respond(HttpResponse.response().withStatusCode(404));

		Config config = new Config().setGithub(new Github().setUrl("http://localhost:1081"));
		GithubClient client = new GithubClient(config);
		Project project = new Project().setOrganization("testOrg");
		Mono<RepoReport> repoReport = client.getRepoReport(project, "testRepo")
				.subscriberContext(Context.of(RequestContext.class, new RequestContext().setCorrelator("testCorr")));
		StepVerifier.create(repoReport)
				.expectErrorMatches(t -> t instanceof WebClientResponseException)
				.verify();
	}
}
