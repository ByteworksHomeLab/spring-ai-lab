package com.ahead.airbnb.services;

import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Service;

/**
 * Service class for handling data ingestion.
 */
@Service
public class IngestionService {

	private final FunctionCatalog catalog;

	/**
	 * Constructs an IngestionService with the given FunctionCatalog.
	 *
	 * @param catalog the FunctionCatalog to use for function lookup
	 */
	public IngestionService(final FunctionCatalog catalog) {
		this.catalog = catalog;
	}

	/**
	 * Performs the ingestion process by looking up and running a composed function.
	 */
	public void ingest() {
		Runnable composedFunction = catalog.lookup(null);
		composedFunction.run();
	}

}
