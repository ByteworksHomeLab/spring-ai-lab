package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.dao.ListingRepository;
import com.byteworksinc.airbnb.entities.Listing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
@ActiveProfiles({"llama3"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("pgvector/pgvector:pg16");

    @Autowired
    private ListingRepository listingRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @Rollback(true)
    @Sql({"/schema.sql"})
    public void testInsertListing() throws ParseException {
        listingRepository.deleteAll();
        Listing listing = ListingBuilder.build();
        Listing result = listingRepository.save(listing);
        assertNotNull(result, "Expected a listing but found none.");
        assertNotNull(listingRepository.findById(result.id()), "Expected to find Listing by ID. but found none.");
        assertEquals(20240617171903L, listing.scrapeId());

    }

}
