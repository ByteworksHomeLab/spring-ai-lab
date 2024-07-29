package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.dao.ListingRepository;
import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.etl.ListingsEtl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/schema.sql"})
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ListingEtlLoadTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("pgvector/pgvector:pg16");

    @Autowired
    private ListingsEtl listingsEtl;

    @Autowired
    private ListingRepository listingDao;

    @Autowired
    JdbcConnectionDetails jdbcConnectionDetails;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }
    @Test
    public void testReadListingCsvFile() {
        try {
            listingDao.deleteAll();
            listingsEtl.readListingCsvFile("data/listings.csv");
            List<Listing> listings = listingDao.findAll();
            assertEquals(15159, listings.size());
        } finally {
            listingDao.deleteAll();
        }
    }
}
