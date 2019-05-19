/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.webfilters;

import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.telefonica.training.webflux.server.domain.RequestContext;
import com.telefonica.training.webflux.server.headers.CustomHeaders;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class CorrelatorWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		RequestContext requestContext = buildRequestContext(request);
		return chain.filter(exchange)
				.subscriberContext(ctxt -> ctxt.put(RequestContext.class, requestContext));
	}

	protected RequestContext buildRequestContext(ServerHttpRequest request) {
		String correlator = getCorrelator(request);
		return new RequestContext()
				.setCorrelator(correlator);
	}

	protected String getCorrelator(ServerHttpRequest request) {
		String correlator = request.getHeaders().getFirst(CustomHeaders.CORRELATOR);
		if (correlator == null) {
			correlator = UUID.randomUUID().toString();
		}
		return correlator;
	}

}
