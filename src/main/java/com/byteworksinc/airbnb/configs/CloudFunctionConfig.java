package com.byteworksinc.airbnb.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class CloudFunctionConfig {

    private static final Logger log = LoggerFactory.getLogger(CloudFunctionConfig.class);

    @Bean
    Function<Flux<byte[]>, Flux<Document>> documentReader() {
        return resourceFlux -> resourceFlux
                .doOnNext(message -> log.info("documentReader() called"))
                .map(fileBytes ->
                        new JsonReader(
                                new ByteArrayResource(fileBytes))
                                .get()
                                .getFirst()).subscribeOn(Schedulers.boundedElastic());
    }

    @Bean
    Function<Flux<Document>, Flux<List<Document>>> documentTransformer() {
        return documentFlux ->
                documentFlux
                        .doOnNext(message -> log.info("documentTransformer() called"))
                        .map(incoming ->
                                new TokenTextSplitter().apply(List.of(incoming))).subscribeOn(Schedulers.boundedElastic());
    }

    @Bean
    Consumer<Flux<List<Document>>> vectorStoreConsumer(VectorStore vectorStore) {
        return documentFlux -> documentFlux
                .doOnNext(message -> log.info("vectorStoreConsumer() called"))
                .doOnNext(documents -> {
                    long docCount = documents.size();
                    log.info("Writing {} documents to vector store.", docCount);

                    vectorStore.accept(documents);

                    log.info("{} documents have been written to vector store.", docCount);
                })
                .subscribe();
    }
}
