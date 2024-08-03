package com.byteworksinc.airbnb.etl;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.simple.JdbcClient;

public class ListingEmbeddingLoader {

    private static final Logger log = LoggerFactory.getLogger(ListingsCSVLoader.class);
    private final JdbcClient jdbcClient;
    private final VectorStore vectorStore;

    public ListingEmbeddingLoader(final JdbcClient jdbcClient, final VectorStore vectorStore) {
        this.jdbcClient = jdbcClient;
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {
        // Credit: Dan Vega - https://www.youtube.com/watch?v=ZoPVGrB8iHU&t=569s
        Integer count = jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class)
                .single();
        log.info("Current count of the Vector Store: {}", count);
        if (count == 0) {

        }
    }

}
