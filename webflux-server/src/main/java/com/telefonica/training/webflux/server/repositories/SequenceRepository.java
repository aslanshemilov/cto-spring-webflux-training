package com.telefonica.training.webflux.server.repositories;

import reactor.core.publisher.Mono;

public interface SequenceRepository<T> {
	Mono<Long> next(Class<T> entityType);

}
