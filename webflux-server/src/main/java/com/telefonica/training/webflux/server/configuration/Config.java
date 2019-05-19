/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.experimental.Accessors;

@Configuration
@ConfigurationProperties(prefix = "webflux-server")
@Data
@Accessors(chain=true)
public class Config {
	private String basePath;
	private Github github;

	@Data
	@Accessors(chain=true)
	public static class Github {
		private String url;
	}
}
