/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.webfilters;

import java.net.URI;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriTemplate;

import reactor.core.publisher.Mono;

/**
 * WebFilter to compose the Location header composing the URI with the request URI and the location header (relative URI).
 */
@Order(4)
@Component
public class CompleteLocationHeaderWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		exchange.getResponse().beforeCommit(() -> {
			HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
			if (httpHeaders.getLocation() != null) {
				URI uriLocation = new UriTemplate("{requestUri}/{locationUri}")
						.expand(exchange.getRequest().getURI(), httpHeaders.getLocation());
				httpHeaders.setLocation(uriLocation);
			}
			return Mono.empty();
		});
		return chain.filter(exchange);
	}
}
