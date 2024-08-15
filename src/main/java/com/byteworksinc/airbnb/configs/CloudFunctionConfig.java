package com.byteworksinc.airbnb.configs;

import com.byteworksinc.airbnb.entities.Listing;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class CloudFunctionConfig {

    private static final Logger log = LoggerFactory.getLogger(CloudFunctionConfig.class);

    public static String asString(Object value) {
        String valueString = "";
        if (value != null) {
            valueString += value.toString();
        }
        return valueString;
    }

    public Document convertJsonNodeToListing(JsonNode jsonNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        Listing listing = objectMapper.convertValue(jsonNode, Listing.class);
        return new Document(
                listing.name() + " - " + listing.description(),
                Map.of(
                        "id", listing.id(),
                        "name", listing.name(),
                        "description", listing.description(),
                        "listingUrl", listing.listingUrl(),
                        "price", listing.price(),
                        "propertyType", listing.propertyType(),
                        "neighborhood", listing.neighbourhood(),
                        "bedrooms", asString(listing.bedrooms()),
                        "bathrooms", asString(listing.bathrooms())
                )
        );
    }

    @Bean
    Function<Flux<byte[]>, Flux<Document>> documentReader() {
        return resourceFlux -> resourceFlux.flatMap(fileBytes -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(fileBytes);
                if (rootNode.isArray()) {
                    // Convert each element of the JSON array into a Document
                    return Flux.fromIterable(rootNode::elements).map(this::convertJsonNodeToListing);
                } else {
                    // If it's not an array, treat the whole content as a single document
                    return Flux.just(convertJsonNodeToListing(rootNode));
                }
            } catch (Exception e) {
                log.error("documentReader error", e);
                return Flux.error(new RuntimeException("Failed to parse JSON", e));
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Bean
    Function<Flux<Document>, Flux<List<Document>>> documentTransformer() {
        return documentsFlux -> documentsFlux.map(incoming -> new TokenTextSplitter().apply(List.of(incoming))).subscribeOn(Schedulers.boundedElastic());
    }

    @Bean
    Consumer<Flux<List<Document>>> vectorStoreConsumer(VectorStore vectorStore) {
        return documentFlux -> documentFlux.doOnNext(vectorStore).subscribe();
    }
}
