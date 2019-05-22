package com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.producer;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;

import com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.domain.SecretMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  PRODUCER: It generates a flux emiting events every 1 second.
 *  Each event  contains a SecretMessage instance with a random fixed length message in lowercase.
 *  
 */
@ConditionalOnExpression("${kafka-application.producer.enabled}")
@EnableBinding(Source.class)
public class KafkaProducer {

	protected static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

	@Value("${kafka-application.consumer.enabled:true}")
	private boolean consumerEnabled;

	@Value("${kafka-application.message.length:10}")
	private int messageLength;

	/**
	 *
	 * When only producing we block for a specific time forcing to generate events.
	 * If we're also consuming we can not block due to interaction with consumer.
	 *
	 */
	@Value("${kafka-application.producer.blocking-time:10}")
	private int blockingTime;

	/**
	 * Producing an event (SecretMessage) each second.
	 *
	 */
	@StreamEmitter
	public void emit(@Output(Source.OUTPUT) FluxSender output) {
		final Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
		final Mono<Void> sender = output.send(interval.map(l -> generateMessage(getMessageLength())));

		if (isConsumerEnabled()) {
			sender.subscribe();
		} else {
			try {
				sender.block(Duration.ofSeconds(getBlockingTime()));
			} catch (final Exception exc) {
				LOGGER.info("Ending emission period of: {} seconds", getBlockingTime());
				System.exit(0);
			}
		}
	}

	SecretMessage generateMessage(int length) {
		final SecretMessage mySecretMessage = new SecretMessage(RandomStringUtils.randomAlphabetic(length).toLowerCase());

		LOGGER.info("Original string: {}", mySecretMessage.getSecretMessage());
		return mySecretMessage;
	}

	/**
	 *
	 * SETTERS & GETTERS
	 *
	 */
	public boolean isConsumerEnabled() {
		return consumerEnabled;
	}

	public void setConsumerEnabled(boolean consumerEnabled) {
		this.consumerEnabled = consumerEnabled;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public int getBlockingTime() {
		return blockingTime;
	}

	public void setBlockingTime(int blockingTime) {
		this.blockingTime = blockingTime;
	}
}
