/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.webfilters;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.telefonica.training.webflux.server.domain.RequestContext;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
@Order(2)
@Component
public class LoggerWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return Mono.subscriberContext()
				.doOnNext(ctxt -> logRequest(exchange, ctxt))
				.then(chain.filter(exchange))
				.doOnEach(signal -> logResponse(exchange, signal.getContext()));
	}

	protected RequestContext getRequestContext(Context ctxt) {
		return ctxt.get(RequestContext.class);
	}

	protected void logRequest(ServerWebExchange exchange, Context ctxt) {
		RequestContext requestContext = getRequestContext(ctxt);
		try {
			MDC.put("trans", requestContext.getTransactionId());
			MDC.put("corr", requestContext.getCorrelator());
			MDC.put("method", exchange.getRequest().getMethodValue());
			MDC.put("path", exchange.getRequest().getPath().value());
			MDC.put("address", getRemoteAddress(exchange));
			log.info("request");
		} catch(Exception e) {
			log.error("Error configuring logRequest MDC. {}: {}", e.getClass().getName(), e.getMessage());
		} finally {
			MDC.clear();
		}
	}

	protected void logResponse(ServerWebExchange exchange, Context ctxt) {
		RequestContext requestContext = getRequestContext(ctxt);
		try {
			MDC.put("trans", requestContext.getTransactionId());
			MDC.put("corr", requestContext.getCorrelator());
			MDC.put("status", getStatusCode(exchange));
			log.info("response");
		} catch (Exception e) {
			log.error("Error configuring logResponse MDC. {}: {}", e.getClass().getName(), e.getMessage());
		} finally {
			MDC.clear();
		}
	}

	protected String getRemoteAddress(ServerWebExchange exchange) {
		try {
			return exchange.getRequest().getRemoteAddress().getAddress().toString();
		} catch (Exception e) {
			return null;
		}
	}

	protected String getStatusCode(ServerWebExchange exchange) {
		try {
			return Integer.toString(exchange.getResponse().getStatusCode().value());
		} catch (Exception e) {
			return null;
		}
	}

}
