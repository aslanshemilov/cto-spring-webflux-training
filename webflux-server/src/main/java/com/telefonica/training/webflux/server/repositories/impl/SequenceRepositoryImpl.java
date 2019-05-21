package com.telefonica.training.webflux.server.repositories.impl;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.telefonica.training.webflux.server.domain.Sequence;
import com.telefonica.training.webflux.server.repositories.SequenceRepository;

import reactor.core.publisher.Mono;

@Repository
public class SequenceRepositoryImpl<T> implements SequenceRepository<T> {

	private final ReactiveMongoTemplate mongoTemplate;
    public SequenceRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
          this.mongoTemplate = mongoTemplate;
    }

	
	@Override
	public Mono<Long> next(Class<T> entityType) {
		
		Query query = new Query(Criteria.where("_id").is(entityType.getName()));
		Update update = new Update().inc("counter", 1);
		FindAndModifyOptions options = new FindAndModifyOptions().upsert(true).returnNew(true);
		return mongoTemplate.findAndModify(query, update, options, Sequence.class)
		.map(Sequence::getCounter);

	}
	
	

}
