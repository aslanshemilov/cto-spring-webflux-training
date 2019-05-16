package com.telefonica.webflux.springcloudstream.kafka.uppercasetransformer.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data @Accessors(chain = true) @AllArgsConstructor @NoArgsConstructor
public class SecretMessage {
	
	private String secretMessage;

}
