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


@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout="45000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProjectsControllerTest {

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
		
		for (TestCase tc : tcs) {
			ResponseSpec resSpec = WebTestClient
					.bindToServer()
					  .baseUrl("http://localhost:8080")
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
}
