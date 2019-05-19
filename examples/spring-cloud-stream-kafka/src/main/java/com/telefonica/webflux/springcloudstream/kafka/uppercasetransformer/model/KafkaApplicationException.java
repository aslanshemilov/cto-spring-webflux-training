package com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.model;

public class KafkaApplicationException extends RuntimeException {

	private static final long serialVersionUID = 6467490222970084607L;

	public KafkaApplicationException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
