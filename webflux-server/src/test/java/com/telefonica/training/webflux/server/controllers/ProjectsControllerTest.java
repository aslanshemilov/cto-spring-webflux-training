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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout="45000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectsControllerTest {

	private ClientAndServer mockServer;
	
	@Before
	public void startServer() {
		mockServer = ClientAndServer.startClientAndServer(1082);
	}

	@After
	public void stopServer() {
		mockServer.stop();
	}

	@Test
	public void validE2ETest() {
		class TestCase {
			String testFile;
			public TestCase(String testFile) {
				this.testFile = testFile;
			}
		}

		List<TestCase> tcs = Arrays.asList(
				new TestCase("src/test/resources/project.json")
		);

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

		WebClient webClient = WebClient.builder()
		.baseUrl("http://localhost:8080")
		.build();
		
		for (TestCase tc : tcs) {
			webClient.post().uri("/api/projects")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromResource(new FileSystemResource(new File(tc.testFile))))
			.exchange()
			.subscribe();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
