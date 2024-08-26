package com.ahead.airbnb.functions;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CsvReaderTest {

	@Value("classpath:/data/test-listings.csv")
	private Resource listingsCSVResource;

	@Autowired
	private Function<Flux<byte[]>, Flux<Document>> csvReader;

	@Test
	public void testFunctionCsvReader() throws IOException {
		// Create sample input documents
		InputStream inputStream = listingsCSVResource.getInputStream();
		Flux<byte[]> byteFlux = Flux.just(inputStream.readAllBytes());
		Function<Flux<byte[]>, Flux<Document>> csvReaderFunction = csvReader;
		Flux<Document> resultFlux = csvReaderFunction.apply(byteFlux);
		StepVerifier.create(resultFlux).assertNext(document -> {
			assertEquals(
					"Walk to 6th, Rainey St and Convention Ctr - Great central  location for walking to Convention Center, Rainey Street, East 6th Street, Downtown, Congress Ave Bats.<br /><br />  Free wifi<br /><br />No Smoking,  No pets",
					document.getContent());
			assertEquals(5456L, document.getMetadata().get("id"));
			assertEquals("Walk to 6th, Rainey St and Convention Ctr", document.getMetadata().get("name"));
			assertEquals(
					"Great central  location for walking to Convention Center, Rainey Street, East 6th Street, Downtown, Congress Ave Bats.<br /><br />  Free wifi<br /><br />No Smoking,  No pets",
					document.getMetadata().get("description"));
			assertEquals("https://www.airbnb.com/rooms/5456", document.getMetadata().get("listingUrl"));
			assertEquals("$108.00", document.getMetadata().get("price"));
			assertEquals("Entire guesthouse", document.getMetadata().get("propertyType"));
		}).assertNext(document -> {
			assertEquals("NW Austin Room - ", document.getContent()); // Description is
																		// blank
			assertEquals(5769L, document.getMetadata().get("id"));
			assertEquals("NW Austin Room", document.getMetadata().get("name"));
			assertEquals("", document.getMetadata().get("description"));
			assertEquals("https://www.airbnb.com/rooms/5769", document.getMetadata().get("listingUrl"));
			assertEquals("$48.00", document.getMetadata().get("price"));
			assertEquals("Private room in home", document.getMetadata().get("propertyType"));
		}).verifyComplete();

	}

}
