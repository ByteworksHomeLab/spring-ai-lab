package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.etl.ListingsEtl;
import com.byteworksinc.airbnb.dao.ListingDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Sql({"/test-schema.ddl"})
public class ListingEtlLoadTest {

    @Autowired
    private ListingsEtl listingsEtl;

    @Autowired
    private ListingDao listingDao;

    @Test
    public void testReadListingCsvFile() {
        listingsEtl.readListingCsvFile("data/listings.csv");
        List<Listing> listings = listingDao.findAll();
        assertEquals(15159, listings.size());
    }
}
