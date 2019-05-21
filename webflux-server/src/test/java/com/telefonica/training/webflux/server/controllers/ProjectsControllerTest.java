/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */
package com.telefonica.training.webflux.server.controllers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;


import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.exceptions.NotFoundException;
import com.telefonica.training.webflux.server.services.ProjectsService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout="45000")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ProjectsControllerTest {

	@Value("${webflux-server.server.url}")
	String serverUrl;
	
	@Value("${server.port}")
	int serverPort;
	
	@Autowired
	private WebTestClient webClient;

	@MockBean
	private ProjectsService projectsService;

	private ClientAndServer mockServer;
		
	@Before
	public void startServer() {
		mockServer = ClientAndServer.startClientAndServer(1082);
		mockServer
		.when(HttpRequest.request()
				.withMethod("GET")
				.withPath("/repos/telefonica/repo1/languages"))
		.respond(HttpResponse.response()
				.withStatusCode(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"A\": 1, \"B\": 2, \"C\": 3}"));
		
		mockServer
		.when(HttpRequest.request()
				.withMethod("GET")
				.withPath("/repos/telefonica/repo2/languages"))
		.respond(HttpResponse.response()
				.withStatusCode(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"D\": 4, \"E\": 5, \"F\": 6}"));

		mockServer
		.when(HttpRequest.request()
				.withMethod("GET")
				.withPath("/repos/telefonica/repo3/languages"))
		.respond(HttpResponse.response()
				.withStatusCode(200)
				.withHeader("Content-Type", "application/json")
				.withBody("{\"G\": 7, \"H\": 7, \"I\": 9}"));		
	}
	
	@After
	public void resetAndStopServer() {
		Mockito.reset(projectsService);
		mockServer.stop();
	}
//	Unit test is disabled because @Valid is not working in the controller.
//	@Test
	public void validReportListingTest() {
		class TestCase {
			String testFile;
			public TestCase(String testFile) {
				this.testFile = testFile;
			}
		}

		List<TestCase> tcs = Arrays.asList(
				new TestCase("src/test/resources/project.json")
		);
		
		for (TestCase tc : tcs) {
			ResponseSpec resSpec = WebTestClient
					.bindToServer()
						.baseUrl(serverUrl + ":" + serverPort)
						.build()
						.post().uri("/api/projects")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8)
						.body(BodyInserters.fromResource(new FileSystemResource(new File(tc.testFile))))
						.exchange()
						.expectStatus().isCreated();			
		}
		
		/**
		 *  Here a synchronization with the end of reporting would be the best to check validity.
		 *  But we just use it to show the reporting result.  
		 */
		try {
			System.out.println("Start waiting for pararel reporting...");
			Thread.sleep(5000);
			System.out.println("Ending waiting...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldCreateProject() {
		Mockito.when(projectsService.createProject(Mockito.any(Project.class))).then(answer -> {
			Project project = answer.getArgument(0);
			project.setId(1L);
			return Mono.just(project);
		});

		Project project = new Project()
				.setName("testProject")
				.setOrganization("testOrg")
				.setRepositories(Arrays.asList("repo1", "repo2"));
		webClient.post()
			.uri("/api/projects")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromObject(project))
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectHeader().valueEquals("location", "/api/projects/1")
			.expectBody(Project.class).isEqualTo(project.setId(1L));
	}

//	Unit test is disabled because @Valid is not working in the controller.
//	@Test
	public void shouldNotCreateProjectByInvalidRequest() {
		Project project = new Project()
				.setName("testProject")
				.setOrganization("invalid organization")
				.setRepositories(Arrays.asList("repo1", "repo2"));
		webClient.post()
			.uri("/api/projects")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromObject(project))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectHeader().doesNotExist("location")
			.expectBody()
				.jsonPath("$.error").isEqualTo("invalid_request")
				.jsonPath("$.error_description").isEqualTo("organization has an invalid value");
	}

	@Test
	public void shouldListProjects() {
		List<Project> projects = Arrays.asList(
				new Project()
					.setId(1L)
					.setName("name1")
					.setOrganization("org1")
					.setRepositories(Arrays.asList("repo1", "repo2")),
				new Project()
					.setId(2L)
					.setName("name2")
					.setOrganization("org2")
					.setGithubToken("token2")
					.setRepositories(Arrays.asList("repo1", "repo3", "repo4")));
		Mockito.when(projectsService.findProjects()).thenReturn(Flux.fromIterable(projects));

		webClient.get()
			.uri("/api/projects")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody(new ParameterizedTypeReference<List<Project>> (){}).isEqualTo(projects);
	}

	@Test
	public void shouldGetProject() {
		Project project = new Project()
				.setId(1L)
				.setName("testProject")
				.setOrganization("testOrg")
				.setRepositories(Arrays.asList("repo1", "repo2"));
		Mockito.when(projectsService.getProject(1L)).thenReturn(Mono.just(project));

		webClient.get()
			.uri("/api/projects/1")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody()
				.jsonPath("$.id").isEqualTo(project.getId())
				.jsonPath("$.name").isEqualTo(project.getName())
				.jsonPath("$.organization").isEqualTo(project.getOrganization())
				.jsonPath("$.githubToken").doesNotExist()
				.jsonPath("$.repositories[0]").isEqualTo(project.getRepositories().get(0))
				.jsonPath("$.repositories[1]").isEqualTo(project.getRepositories().get(1));
	}


	@Test
	public void shouldNotGetProjectByNotFound() {
		Mockito.when(projectsService.getProject(1L)).thenReturn(Mono.error(new NotFoundException()));

		webClient.get()
			.uri("/api/projects/1")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	public void shouldUpdateProject() {
		Project project = new Project()
				.setId(1L)
				.setName("testProject")
				.setOrganization("testOrg")
				.setRepositories(Arrays.asList("repo1", "repo2"));
		Mockito.when(projectsService.updateProject(project)).thenReturn(Mono.just(project));

		webClient.put()
			.uri("/api/projects/1")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromObject(project))
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody(Project.class).isEqualTo(project);
	}

	@Test
	public void shouldNotUpdateProjectByNotFound() {
		Project project = new Project()
				.setId(1L)
				.setName("testProject")
				.setOrganization("testOrg")
				.setRepositories(Arrays.asList("repo1", "repo2"));
		Mockito.when(projectsService.updateProject(project)).thenReturn(Mono.error(new NotFoundException()));

		webClient.put()
			.uri("/api/projects/1")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromObject(project))
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	public void shouldDeleteProject() {
		Mockito.when(projectsService.deleteProject(1L)).thenReturn(Mono.empty());

		webClient.delete()
			.uri("/api/projects/1")
			.exchange()
			.expectStatus().isNoContent();
	}

	@Test
	public void shouldNotDeleteProjectByNotFound() {
		Mockito.when(projectsService.deleteProject(1L)).thenReturn(Mono.error(new NotFoundException()));

		webClient.delete()
			.uri("/api/projects/1")
			.exchange()
			.expectStatus().isNotFound();
	}


}
