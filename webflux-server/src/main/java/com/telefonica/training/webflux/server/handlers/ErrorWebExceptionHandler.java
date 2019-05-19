/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.handlers;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.telefonica.training.webflux.server.domain.ServerError;
import com.telefonica.training.webflux.server.exceptions.ResponseException;
import com.telefonica.training.webflux.server.exceptions.ServerException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class ErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	public ErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
			ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
		super(errorAttributes, resourceProperties, applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderServerResponse);
	}

	protected Mono<ServerResponse> renderServerResponse(ServerRequest request) {
		ResponseException e = getResponseException(request);
		BodyBuilder bodyBuilder = ServerResponse.status(e.getStatus())
				.headers(headers -> e.getHeaders());
		if (e.getError() == null) {
			return bodyBuilder.build();
		}
		return bodyBuilder.body(BodyInserters.fromObject(new ServerError(e.getError(), e.getReason())));
	}

	protected ResponseException getResponseException(ServerRequest request) {
		Throwable t = getError(request);
		if (t instanceof ResponseException) {
			return (ResponseException) t;
		}
		if (t instanceof WebExchangeBindException) {
			WebExchangeBindException e = (WebExchangeBindException) t;
			return new ResponseException(e.getStatus(), "invalid_request", e.getFieldError().getDefaultMessage());
		}
		if (t instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException)t;
			return new ResponseException(responseStatusException.getStatus());
		}
		log.error("Unhandled exception {}: {}", t.getClass().getName(), t.getMessage());
		return new ServerException(t);
	}

}
