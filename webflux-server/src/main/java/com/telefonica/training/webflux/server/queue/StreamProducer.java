/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * Publishes all the notifications passed with {@link Notifiable#produce(Object, Object)} in the queue.
 *
 */
@ConditionalOnExpression("${KafkaApplication.producer.enabled}")
@EnableBinding(Source.class)
public class StreamProducer implements Notifiable {

	protected static final Logger LOGGER = LoggerFactory.getLogger(StreamProducer.class);

	private final Flux<Notification> flux;

	private FluxSink<Notification> fluxSink;

	/**
	 * Constructor.
	 */
	public StreamProducer() {
		this.flux = Flux.<Notification>create(
							emitter -> this.fluxSink = emitter,
							FluxSink.OverflowStrategy.IGNORE)
					.publish()
					.autoConnect();
	}

	@StreamEmitter
	public void emit(@Output(Source.OUTPUT) FluxSender output) {
		output.send(this.flux);
	}

	public <T> void produce(T old, T current) {
		Notification notification = new Notification(old, current);
		if (fluxSink == null) {
			LOGGER.error("Producer flux sink not created yet. Discarding notification: {}", notification);
			return;
		}
		LOGGER.info("Sending notification: {}", notification);
		try {
			fluxSink.next(notification);
		} catch (Exception e) {
			LOGGER.error("Error sending notification: {}", notification, e);
		}
	}

	@Override
	public <T> void inserted(T current) {
		produce(null, current);
	}

	@Override
	public <T> void deleted(T old) {
		produce(old, null);
	}

	@Override
	public <T> void updated(T old, T current) {
		produce(old, current);
	}

}
