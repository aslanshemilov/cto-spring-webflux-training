package com.telefonica.training.webflux.server.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

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

	/**
	 * Whenever a Flux of Notification is received it is processed to distinguish among an insert, delete or update
	 * and call to the appropiated functions. 
	 */
	private Mono<Boolean> process(Flux<Notification> input) {
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
		retreiveReport(current).subscribe();
	}

	@Override
	public <T> void deleted(T old) {
		LOGGER.info("Deleted called");	
	}

	@Override
	public <T> void updated(T old, T current) {
		LOGGER.info("Updated called");	
		retreiveReport(current).subscribe();
	}
	
	/*
	 * Go through the project repos and for each of them within a flux streaming recover its technology information from github.
	 * All the information recovered is stored in a map for a later printing.
	 * 
	 */
	private <T> Mono<Boolean> retreiveReport(T element) {
		Map<String, List<Tuple2<String, Integer>>> report = new HashMap<>();		
		if (element instanceof Project) {
			Project project = (Project) element;
			List<String> repos = project.getRepositories();
			if (repos == null || repos.isEmpty()) {
				return Mono.just(true);
			}
			return Flux.fromIterable(repos)
					.flatMap(repoName -> Mono.just(Tuples.of(repoName,
													webServerClient.getRepoReport(project.getId(), repoName)
														.switchIfEmpty(Mono.just(new RepoReport()))
														.onErrorReturn(new RepoReport()))
										)
					)
					.flatMap(tuple -> tuple.getT2()
							.flatMapMany(repo -> {
								if (repo.getLanguages() == null || repo.getLanguages().isEmpty()) {
									return Mono.empty();
								}
								return Flux.fromIterable(repo.getLanguages().entrySet())
									.flatMap(language -> {
										List<Tuple2<String, Integer>> list = report.get(tuple.getT1());
										if (list == null) {
											list = new ArrayList<>();
										}
										list.add(Tuples.of(language.getKey(), language.getValue()));
										report.put(tuple.getT1(), list);
										return Flux.just(language);
									});							
							})
					)
					.doAfterTerminate(() -> listReport(project, report))
					.then(Mono.just(true));			
		}
		LOGGER.error("Error with notification received: {}", element);		
		return Mono.just(false);
	}
	
	/**
	 * Traversal the map printing the report information.
	 */
	private void listReport(Project project, Map<String, List<Tuple2<String, Integer>>> report) {
		LOGGER.info("Project {} repos info:", project.getName());
		for (Map.Entry<String, List<Tuple2<String, Integer>>> entry : report.entrySet()) {
			LOGGER.info("	- Repo {} info:", entry.getKey());
			for (Tuple2<String, Integer> tuple: entry.getValue()) {
				LOGGER.info("		- Repo: ({}, {}) ", tuple.getT1(), tuple.getT2());
			}
		} 		
	}
	
}
