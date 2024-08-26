package com.ahead.airbnb.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SpringBootTest
public class DocumentTransformerTest {

	@Autowired
	private Function<Flux<Document>, Flux<List<Document>>> documentTransformer;

	@Test
	public void testDocumentTransformer() {
		// Create sample input documents
		String document1Name = "Bright S.Austin Property~ Central~3 bed's 2 Bath";
		String document1Desc = "Charming mid century property.  2 miles from Town Lake and downtown, this place is a haven from the hustle and bustle.  With city views from the yard, it is within walking distance to S. Congress and Restaurants. Whether you want to relax in the private yard or go out for a hike, it is an ideal location.   The house is flooded with light and very quiet.\",\"neighborhoodOverview\":\"The residential property is on a hill with city views and folks routinely loop around the neighborhood for a good workout.   Additionally, there is nice pocket park just five minutes walk away.  Town Lake (Lady Bird Lake) is 2 miles north. There are numerous restaurants within walking distance and South Congress is minutes away.";
		Document document1 = new Document(document1Name + " - " + document1Desc, Map.of("id", "47783340", "name",
				document1Name, "description",
				"Charming mid century property.  2 miles from Town Lake and downtown, this place is a haven from the hustle and bustle.  With city views from the yard, it is within walking distance to S. Congress and Restaurants. Whether you want to relax in the private yard or go out for a hike, it is an ideal location.   The house is flooded with light and very quiet.\",\"neighborhoodOverview\":\"The residential property is on a hill with city views and folks routinely loop around the neighborhood for a good workout.   Additionally, there is nice pocket park just five minutes walk away.  Town Lake (Lady Bird Lake) is 2 miles north. There are numerous restaurants within walking distance and South Congress is minutes away.",
				"listingUrl", "https://www.airbnb.com/rooms/47783340", "price", "$135.00", "propertyType",
				"Entire home", "neighborhood", "Neighborhood highlights", "bedrooms", "3", "bathrooms", "3"));

		String document2Name = "Bright, comfy and centrally located 1br apartment";
		String document2Desc = "1 br/1 bath apt - 7 minutes to downtown and East side of Austin. <br />- Queen bed for 2 people and couch for 1. <br />- Desk for remote work space<br />- 2 blocks South of the river where boardwalk starts. Paddle board and kayak/canoe rentals here. <br />- City bike rental down the block, scooters scattered around the neighborhood.<br />- Next door to Fraziers (bar/restaurant) and convenience store.";
		Document document2 = new Document(document2Name + " - " + document2Desc,
				Map.of("id", "46638773", "name", document2Name, "description", document2Desc, "listingUrl",
						"https://www.airbnb.com/rooms/46638773", "price", "$100.00", "propertyType",
						"Entire rental unit", "neighborhood", "East Riverside - Oltorf", "bedrooms", "1", "bathrooms",
						"1"));

		Flux<Document> documentsFlux = Flux.just(document1, document2);

		Flux<List<Document>> resultFlux = documentTransformer.apply(documentsFlux);

		StepVerifier.create(resultFlux).consumeNextWith(documents -> {
			Assertions.assertNotNull(documents, "Expected documents list but got none");
			Assertions.assertEquals(document1Name + " - " + document1Desc, documents.getFirst().getContent(),
					"Content does not match");
		}).consumeNextWith(documents -> {
			Assertions.assertNotNull(documents, "Expected documents list but got none");
			Assertions.assertEquals(document2Name + " - " + document2Desc, documents.getFirst().getContent(),
					"Content does not match");
		}).verifyComplete();
	}

}
