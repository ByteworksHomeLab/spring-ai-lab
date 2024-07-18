package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.etl.ListingsEtl;
import com.byteworksinc.airbnb.repositories.ListingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ListingEtlLoadTest {

    @Autowired
    private ListingsEtl listingsEtl;

    @Autowired
    private ListingRepository listingRepository;

    @Test
    public void testReadListingCsvFile() {
        listingsEtl.readListingCsvFile("data/listings.csv");
        List<Listing> listings = listingRepository.findAll();
        assertEquals(15160, listings.size());
    }
}
