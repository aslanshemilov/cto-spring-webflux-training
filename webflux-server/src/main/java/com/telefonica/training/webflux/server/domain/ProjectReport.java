package com.telefonica.training.webflux.server.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import reactor.util.function.Tuple2;

@Document(collection = ProjectReport.COLLECTION)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @Accessors(chain=true) @AllArgsConstructor
public class ProjectReport {

	public static final String COLLECTION = "projectReport";
	
	@Id
	private String projectName;
	
	Map<String, List<Tuple2<String, Integer>>> repositoryLanguages;
	
}
