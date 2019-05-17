package com.telefonica.training.webflux.server.queue;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.telefonica.training.webflux.server.domain.Project;
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
					return null;
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
		listRepos(current);
	}

	@Override
	public <T> void deleted(T old) {
		LOGGER.info("Deleted called");	
	}

	@Override
	public <T> void updated(T old, T current) {
		LOGGER.info("Updated called");	
		listRepos(current);
	}
	
	public <T> Mono<Boolean> listRepos(T element) {
		if (element == null) {
			LOGGER.error("Null element received");
			return Mono.just(false);
		}
		if (element instanceof Project) {
			Project project = (Project) element;
			LOGGER.info("Project {} repos info:", project.getName());
			return Flux.fromIterable(project.getRepositories())
					.flatMap(repoName -> {
						LOGGER.info("Repo {} info:", repoName);
						return webServerClient.getRepoReport(project.getId(), repoName);
					})
					.flatMap(repo -> {
						if (repo.getLanguages() == null || repo.getLanguages().isEmpty()) {
							return Mono.empty();
						}
						return Flux.fromIterable(repo.getLanguages().entrySet())
								.flatMap(language -> {
									LOGGER.info("Repo ({}, {}): ", language.getKey(), language.getValue());
									return Mono.empty();
								});
					})
					.then(Mono.just(true));			
		}
		LOGGER.error("Not a project object notified: {}", element);		
		return Mono.just(false);
	}
	
}
