/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.telefonica.training.webflux.server.domain.ServerError;
import com.telefonica.training.webflux.server.exceptions.NotFoundException;

@ControllerAdvice
public class ErrorControllerAdvice {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.build();
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ServerError> handleResponseStatusException(ResponseStatusException e) {
		return ResponseEntity.status(e.getStatus())
				.body(new ServerError("spring-error", e.getReason()));
	}

}
