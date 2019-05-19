/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResponseException extends RuntimeException {
	private static final long serialVersionUID = -7053061576358356336L;

	private final HttpStatus status;
	private final String error;
	private final String reason;
	private final HttpHeaders headers;

	public ResponseException(HttpStatus status) {
		this(status, null, null);
	}

	public ResponseException(HttpStatus status, String error, String reason) {
		this(status, error, reason, null);
	}

	public ResponseException(HttpStatus status, String error, String reason, Throwable t) {
		super(t);
		this.status = status;
		this.error = error;
		this.reason = reason;
		this.headers = new HttpHeaders();
	}

	public void addHeader(String headerName, String headerValue) {
		headers.add(headerName, headerValue);
	}
}
