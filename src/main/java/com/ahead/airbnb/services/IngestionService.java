package com.ahead.airbnb.services;

import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Service;

@Service
public class IngestionService {

	private final FunctionCatalog catalog;

	public IngestionService(final FunctionCatalog catalog) {
		this.catalog = catalog;
	}

	public void ingest() {
		Runnable composedFunction = catalog.lookup(null);
		composedFunction.run();
	}

}
