/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.repositories;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.telefonica.training.webflux.server.domain.Project;

import reactor.core.publisher.Flux;


@Repository
public interface ProjectsRepository extends ReactiveMongoRepository<Project,Long>, SequenceRepository<Project> {

	@Query(value="{}", sort="{_id: 1}")
	Flux<Project> getProjectsPaginated(Pageable pageable);


}
