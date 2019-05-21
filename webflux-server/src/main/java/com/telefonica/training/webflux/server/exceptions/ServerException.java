/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.exceptions;

import org.springframework.http.HttpStatus;

public class ServerException extends ResponseException {

	private static final long serialVersionUID = 390003906343824691L;

	public ServerException(Throwable t) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, "server_error", t.getMessage());
	}

}
