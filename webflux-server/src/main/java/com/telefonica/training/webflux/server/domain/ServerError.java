/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerError {
	/**
	 * Error identifier.
	 */
	@JsonProperty("error")
	private String error;

	/**
	 * Error description.
	 */
	@JsonProperty("error_description")
	private String reason;
}
