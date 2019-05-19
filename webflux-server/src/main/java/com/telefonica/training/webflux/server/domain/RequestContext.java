/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class RequestContext {
	private String correlator;
}
