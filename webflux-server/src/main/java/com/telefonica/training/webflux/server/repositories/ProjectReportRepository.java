/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.telefonica.training.webflux.server.domain.ProjectReport;

@Repository
public interface ProjectReportRepository extends ReactiveMongoRepository<ProjectReport, String> {
}
