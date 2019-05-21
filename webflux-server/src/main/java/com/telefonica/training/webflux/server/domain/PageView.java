package com.telefonica.training.webflux.server.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageView {
	
	private int page;
	private int size;
	private Long total;
	private List<Project> projects;

}
