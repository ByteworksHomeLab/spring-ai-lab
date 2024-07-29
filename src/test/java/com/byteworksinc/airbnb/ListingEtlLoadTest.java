package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.dao.ListingRepository;
import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.etl.ListingsEtl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Rollback(true)
@Sql({"/schema.sql"})
@Testcontainers
public class ListingEtlLoadTest {

    @Autowired
    private ListingsEtl listingsEtl;

    @Autowired
    private ListingRepository listingDao;

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
