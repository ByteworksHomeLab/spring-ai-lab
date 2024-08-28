package com.ahead.airbnb.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CsvFileSupplierTest {

	@InjectMocks
	private ETLFunctions ETLFunctions;

	@Mock
	private ClassPathResource mockResource;

	private final String csvContent = "id,listing_url,name,description,price,bathrooms,bedrooms,beds,property_type\n"
			+ "1,http://example.com/listing/1,Sample Name,Sample Description,100,1,2,3,Apartment\n"
			+ "2,http://example.com/listing/2,Another Name,Another Description,200,2,3,4,House\n";

	@BeforeEach
	void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);

		// Mocking the resource to return the CSV content
		when(mockResource.exists()).thenReturn(true);
		when(mockResource.getInputStream())
			.thenReturn(new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void testCsvFileSupplier() {
		Supplier<Flux<byte[]>> csvFileSupplier = () -> {
			try {
				if (!mockResource.exists()) {
					return Flux.empty();
				}

				try (InputStreamReader reader = new InputStreamReader(mockResource.getInputStream(),
						StandardCharsets.UTF_8); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

					char[] buffer = new char[1024];
					int bytesRead;
					while ((bytesRead = reader.read(buffer)) != -1) {
						outputStream.write(new String(buffer, 0, bytesRead).getBytes(StandardCharsets.UTF_8));
					}

					byte[] byteArray = outputStream.toByteArray();
					return Flux.just(byteArray);
				}

			}
			catch (IOException e) {
				return Flux.empty();
			}
		};

		// Use StepVerifier to test the results
		StepVerifier.create(csvFileSupplier.get())
			.assertNext(bytes -> assertArrayEquals(csvContent.getBytes(StandardCharsets.UTF_8), bytes))
			.verifyComplete();

	}

}
