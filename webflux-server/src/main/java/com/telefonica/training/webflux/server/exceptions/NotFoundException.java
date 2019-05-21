/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ResponseException {

	private static final long serialVersionUID = -7494029162138204728L;

	public NotFoundException() {
		super(HttpStatus.NOT_FOUND);
	}

}
