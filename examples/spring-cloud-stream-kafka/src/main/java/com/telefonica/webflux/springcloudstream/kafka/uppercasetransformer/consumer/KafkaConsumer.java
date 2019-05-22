package com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.domain.SecretMessage;
import com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.exceptions.KafkaApplicationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  CONSUMER: It receives a Flux of SecretMessage and converts each message to uppercase.
 *  
 */
@ConditionalOnExpression("${kafka-application.consumer.enabled}")
@EnableBinding(Sink.class)
public class KafkaConsumer {

	protected static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private boolean withError = false;

	public KafkaConsumer(@Value("${kafka-application.consumer.with-error}") boolean withError) {
		this.withError = withError;
	}

	@StreamListener
	public void receive(@Input(Sink.INPUT) Flux<SecretMessage> input) {	
//	@StreamListener(Sink.INPUT)
//	public void receive(SecretMessage input) {
		process(input).subscribe();
	}

	public Mono<Boolean> process(Flux<SecretMessage> input) {
//	public Mono<Boolean> process(SecretMessage input) {		
		return input.map(message -> {
//		return Flux.just(input).map(message -> {			
					final String myMessage = message.getSecretMessage();

					if (isWithError() && myMessage.contains("a")) {
						throw new KafkaApplicationException("Error on: " + myMessage, null);
					}
					LOGGER.info("Transformed string: {}", myMessage.toUpperCase());
					return myMessage.toUpperCase();
			})	
			.onErrorContinue((exc, element) -> LOGGER.error("Error consuming message: {}", exc.getMessage()))
			.then(Mono.just(true));
	}

	/**
	 *
	 * SETTER & GETTER
	 *
	 */
	public boolean isWithError() {
		return withError;
	}

	public void setWithError(boolean withError) {
		this.withError = withError;
	}
}
