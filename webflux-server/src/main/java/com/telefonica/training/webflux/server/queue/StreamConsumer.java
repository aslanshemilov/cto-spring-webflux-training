package com.telefonica.training.webflux.server.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.telefonica.training.webflux.server.domain.Project;
import com.telefonica.training.webflux.server.domain.RepoReport;
import com.telefonica.training.webflux.server.client.WebServerClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ConditionalOnExpression("${KafkaApplication.consumer.enabled}")
@EnableBinding(Sink.class)
public class StreamConsumer implements Notifiable {

	protected static final Logger LOGGER = LoggerFactory.getLogger(StreamConsumer.class);
	private final WebServerClient webServerClient;
	
	public StreamConsumer(WebServerClient webServerClient) {
		this.webServerClient = webServerClient;
	}
	
	@StreamListener
	public void receive(@Input(Sink.INPUT) Flux<Notification> input) {
		process(input).subscribe();
	}

	public Mono<Boolean> process(Flux<Notification> input) {
		return input.map(notification -> {
					LOGGER.info("Processing notification: {}", notification);
					if (notification.getOld() == null) {
						if (notification.getCurrent() != null) {
							inserted(notification.getCurrent());
						} else {
							LOGGER.error("Error consuming notification: {}", notification);
						}
					} else if (notification.getCurrent() == null) {
						deleted(notification.getOld());
					} else {
						updated(notification.getOld(), notification.getCurrent());
					}
					return Flux.empty();
			})
			.onErrorResume(exc -> {
					LOGGER.error("Error consuming message: {}", exc.getMessage());
					return Mono.error(exc);
			})
			.then(Mono.just(true));
	}
	
	@Override
	public <T> void inserted(T current) {
		LOGGER.info("Inserted called");
		listRepos(current).subscribe();
	}

	@Override
	public <T> void deleted(T old) {
		LOGGER.info("Deleted called");	
	}

	@Override
	public <T> void updated(T old, T current) {
		LOGGER.info("Updated called");	
		listRepos(current).subscribe();
	}
	
	public <T> Mono<Boolean> listRepos(T element) {
		if (element == null) {
			LOGGER.info("Null element received");
			return Mono.just(true);
		}
		if (element instanceof Project) {
			Project project = (Project) element;
			LOGGER.info("Project {} repos info:", project.getName());
			List<String> repos = project.getRepositories();
			if (repos == null || repos.isEmpty()) {
				LOGGER.info("	- No repositories");
				return Mono.just(true);
			}
			return Flux.fromIterable(repos)
					.flatMap(repoName -> {
						LOGGER.info("	- Repo {} info:", repoName);
						Mono<RepoReport> report = webServerClient.getRepoReport(project.getId(), repoName)
								.switchIfEmpty(Mono.just(new RepoReport()));
						return report;
					})
					.flatMap(repo -> {
						if (repo.getLanguages() == null || repo.getLanguages().isEmpty()) {
							LOGGER.info("No languages");							
							return Flux.empty();
						}
						return Flux.fromIterable(repo.getLanguages().entrySet())
								.flatMap(language -> {
									LOGGER.info("		- Repo: ({}, {}) ", language.getKey(), language.getValue());
									return Flux.empty();
								});
					})
					.then(Mono.just(true));			
		}
		LOGGER.error("Not a project object notified: {}", element);		
		return Mono.just(false);
	}
	
}
