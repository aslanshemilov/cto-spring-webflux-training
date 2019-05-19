/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

	private static final long serialVersionUID = -7494029162138204728L;

	public NotFoundException() {
		super(HttpStatus.NOT_FOUND, "Not found");
	}

}
