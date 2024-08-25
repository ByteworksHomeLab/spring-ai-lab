package com.ahead.airbnb.functions;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class EtlCloudFunctions {

	private static final Logger log = LoggerFactory.getLogger(EtlCloudFunctions.class);

	private final String[] headers = { "id", "listing_url", "name", "description", "neighbourhood", "property_type",
			"bathrooms", "bedrooms", "beds", "price", };

	public static Long getLong(CSVRecord record, String columnName) {
		if (record != null && columnName != null) {
			String value = record.get(columnName);
			if (value != null && !value.isEmpty()) {
				try {
					return Long.parseLong(value);
				}
				catch (NumberFormatException e) {
					log.warn("Could not parse Float {}", value);
				}
			}
		}
		return null;
	}

	@Bean
	public Supplier<Flux<byte[]>> csvFileSupplier() {
		return () -> {
			try {
				String resourceName = "/data/listings.csv";
				ClassPathResource resource = new ClassPathResource(resourceName);
				if (!resource.exists()) {
					log.error("Resource {} does not exist", resourceName);
					return Flux.empty();
				}
				try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(),
						StandardCharsets.UTF_8); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

					char[] buffer = new char[1024];
					int bytesRead;
					while ((bytesRead = reader.read(buffer)) != -1) {
						outputStream.write(new String(buffer, 0, bytesRead).getBytes(StandardCharsets.UTF_8));
					}
					return Flux.just(outputStream.toByteArray());
				}
			}
			catch (IOException e) {
				log.error("Error reading resource", e);
				return Flux.empty();
			}
		};
	}

	@Bean
	public Function<Flux<byte[]>, Flux<Document>> csvReader() {
		return byteFlux -> byteFlux.publishOn(Schedulers.boundedElastic()).flatMap(bytes -> {
			try {
				Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
				Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(headers)
					.withFirstRecordAsHeader()
					.parse(reader);
				return Flux.fromIterable(records).map(record -> {
					String name = record.get("name");
					String description = record.get("description");
					return new Document(name + " - " + description,
							Map.of("id", EtlCloudFunctions.getLong(record, "id"), "name", name, "description",
									description, "listingUrl", record.get("listing_url"), "price", record.get("price"),
									"propertyType", record.get("property_type"), "neighborhood",
									record.get("neighbourhood"), "bedrooms", record.get("bedrooms"), "bathrooms",
									record.get("bathrooms"), "beds", record.get("beds")));
				}).onErrorContinue((throwable, record) -> log.error("Error parsing document: {}", record, throwable));
			}
			catch (Exception e) {
				return Flux.empty();
			}
		}).onErrorResume(throwable -> {
			log.error("Error processing the byte flux", throwable);
			return Flux.empty();
		});
	}

	@Bean
	Function<Flux<Document>, Flux<List<Document>>> documentTransformer() {
		return documentsFlux -> documentsFlux.map(document -> new TokenTextSplitter().apply(List.of(document)))
			.onErrorContinue((throwable, document) -> log.error("Error transforming document: {}", document, throwable))
			.subscribeOn(Schedulers.boundedElastic());
	}

	@Bean
	Consumer<Flux<List<Document>>> vectorStoreConsumer(VectorStore vectorStore) {
		return documentFlux -> documentFlux
			.flatMap(documents -> Flux.fromIterable(documents)
				.doOnNext(nextDocument -> vectorStore.accept(List.of(nextDocument)))
				.onErrorContinue((throwable, document) -> log.error("Error storing document: {}", document, throwable))
				.collectList())
			.subscribe();
	}

}
