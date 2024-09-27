package com.ahead.airbnb.controllers;

import com.ahead.airbnb.services.IngestionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The purpose of this class is to provide an endpoint for ingesting data from the Airbnb
 * CSV file.
 */
@RestController
public class ETLController {

	private final IngestionService ingestionService;

	public ETLController(IngestionService ingestionService) {
		this.ingestionService = ingestionService;
	}

	/**
	 * Ingests data from the Airbnb CSV file into the vector store. This process runs for
	 * about an hour.
	 * @return a response entity indicating the ingestion process has started
	 */
	@Operation(summary = "Ingest data from the Airbnb CSV file into the vector store. It runs for about an hour.")
	@GetMapping("/run-ingestion")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> ingest() {
		ingestionService.ingest();
		return ResponseEntity.accepted().build();
	}

}
