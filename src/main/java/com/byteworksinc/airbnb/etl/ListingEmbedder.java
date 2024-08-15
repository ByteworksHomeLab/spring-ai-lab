package com.byteworksinc.airbnb.etl;

import com.byteworksinc.airbnb.entities.Listing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Component
public class ListingEmbedder {

    private static final Logger log = LoggerFactory.getLogger(ListingEmbedder.class);

    private final VectorStore vectorStore;

    public ListingEmbedder(final VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public static String asString(Object value) {
        String valueString = "";
        if (value != null) {
            valueString += value.toString();
        }
        return valueString;
    }

    public void embedListing(final List<Listing> listings) {
        log.info("Embedding listings");
        listings.parallelStream().forEach(listing -> {
            var document = new Document(
                    listing.name() + ": " + listing.description(),
                    Map.of("id", listing.id(),
                            "name", listing.name(),
                            "description", listing.description(),
                            "listingUrl", listing.listingUrl(),
                            "price", listing.price(),
                            "propertyType", listing.propertyType(),
                            "neighborhood", listing.neighbourhood(),
                            "bedrooms", asString(listing.bedrooms()),
                            "bathrooms", asString(listing.bathrooms())
                    ));
            try {
                var split = new TokenTextSplitter()
                        .apply(List.of(document));
                vectorStore.add(split);
            } catch (RuntimeException e) {
                String message = String.format("Could not embed listing %s because %s", listing.id(), e.getMessage());
                log.error(message, e);
            }
        });
    }
}
