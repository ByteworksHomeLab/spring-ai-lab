package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.controllers.ListingController;
import com.byteworksinc.airbnb.entities.Listing;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ListingControllerTest {

    @Autowired
    private ListingController listingController;

    @Test
    public void testInsertListing() {
        Listing listing = new Listing();
        listing.setId(1234567l);
        listing.setLatitude(BigDecimal.valueOf(30.26057));
        listing.setLongitude(BigDecimal.valueOf(-97.73441));
        listing.setPropertyType("Entire guesthouse");
        listing.setRoomType("Entire home/apt");
        listing.setAccommodates(3);
        listing.setBathrooms(1.0d);
        listing.setBathroomsText("1 bath");
        listing.setBedrooms(1);
        listing.setBeds(2);
        listing.setScrapeId(20240617171903L);

        Listing result = listingController.insertListing(listing);
        assertNotNull(result, "Expected a listing but found none.");
        assertNotNull(result.getId(), "Expected a listing to have an ID, but found none.");
        assertNotNull(listingController.findById(result.getId()), "Expected to find Listing by ID. but found none.");
        assertEquals(20240617171903L, listing.getScrapeId());
    }

}
