package com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.model.KafkaApplicationException;
import com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.model.SecretMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  CONSUMER: It receives a Flux of SecretMessage and converts each message to uppercase.
 *  
 */
@ConditionalOnExpression("${KafkaApplication.consumer.enabled}")
@EnableBinding(Sink.class)
public class KafkaConsumer {

	protected static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private boolean withError = false;

	public KafkaConsumer(@Value("${KafkaApplication.consumer.withError}") boolean withError) {
		this.withError = withError;
	}

	@StreamListener
	public void receive(@Input(Sink.INPUT) Flux<SecretMessage> input) {
		process(input).subscribe();
	}

	public Mono<Boolean> process(Flux<SecretMessage> input) {
		return input.map(message -> {
					final String myMessage = message.getSecretMessage();

					if (isWithError() && myMessage.contains("a")) {
						throw new KafkaApplicationException("Error on: " + myMessage, null);
					}
					LOGGER.info("Transformed string: {}", myMessage.toUpperCase());
					return myMessage.toUpperCase();
			})
			.onErrorResume(exc -> {
					LOGGER.error("Error consuming message: {}", exc.getMessage());
					return Mono.error(exc);
			})
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
