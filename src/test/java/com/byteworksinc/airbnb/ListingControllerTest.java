package com.byteworksinc.airbnb;

import com.byteworksinc.airbnb.controllers.ListingController;
import com.byteworksinc.airbnb.dao.ListingDao;
import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.etl.ListingsEtl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Rollback(true)
public class ListingControllerTest {

    @Autowired
    private ListingDao listingDao;

    @Autowired
    private ListingController listingController;

    @Autowired
    private ListingsEtl listingsEtl;

    @Test
    @Rollback(true)
    @Sql({"/test-schema.ddl"})
    public void testInsertListing() {
        listingDao.deleteAll();
        Listing listing = new Listing();
        listing.setId(1234567L);
        listing.setListingUrl("https://www.airbnb.com/rooms/5456");
        listing.setScrapeId(20240617171903L);
        listing.setLastScraped(listingsEtl.convertDate("2024-06-18"));
        listing.setSource("city scrape");
        listing.setName("Walk to 6th, Rainey St and Convention Ctr");
        listing.setDescription("Great central  location for walking to Convention Center, Rainey Street, East 6th Street, Downtown, Congress Ave Bats.<br /><br />  Free wifi<br /><br />No Smoking,  No pets'");
        listing.setNeighborhoodOverview("My neighborhood is ideally located if you want to walk to bars and restaurants downtown, East 6th Street or Rainey Street.  The Convention Center is only 3 1/2 blocks away and a quick 10 minute walk. Whole foods store located 5 blks , easily walkable.");
        listing.setPictureUrl("https://a0.muscache.com/pictures/14084884/b5a35a84_original.jpg");
        listing.setHostId(8028L);
        listing.setHostUrl("https://www.airbnb.com/users/show/8028");
        listing.setHostName("Sylvia");
        listing.setHostSince(listingsEtl.convertDate("2009-02-16"));
        listing.setHostLocation("Austin, TX");
        listing.setHostAbout("I am a licensed Real Estate Broker and owner of Armadillo Realty.  I attended The University of Texas at Austin and fell in love with the small town that it was back in 1979; I have been here every since.  I love the Art, Music and Film scene here in Austin.  There is so much natural beauty to enjoy as well. I especially enjoy Barton Springs Pool in the summertime along with the Zilker Hillside theater productions. SXSW, Austin City Limits Festival and the East Austin Art Studio Tour are among my favorite events. I also enjoy a sunset cruise on my canoe to Congress bridge to see the Mexican Freetail Bats come out for their nightly feeding.  ");
        listing.setHostResponseTime("within an hour");
        listing.setHostResponseRate("100%");
        listing.setHostAcceptanceRate("100%");
        listing.setHostIsSuperhost(true);
        listing.setHostThumbnailUrl("https://a0.muscache.com/im/users/8028/profile_pic/1329882962/original.jpg?aki_policy=profile_small");
        listing.setHostPictureUrl("https://a0.muscache.com/im/users/8028/profile_pic/1329882962/original.jpg?aki_policy=profile_x_medium");
        listing.setHostNeighbourhood("East Downtown");
        listing.setHostListingsCount(1);
        listing.setHostTotalListingsCount(1);
        listing.setHostHasProfilePic(true);
        listing.setHostIdentityVerified(true);
        listing.setNeighbourhood("Neighborhood highlights");
        listing.setHostVerifications("['email', 'phone']");
        listing.setLatitude(BigDecimal.valueOf(30.26057).setScale(5, RoundingMode.HALF_UP));
        listing.setLongitude(BigDecimal.valueOf(-97.73441).setScale(5, RoundingMode.HALF_UP));
        listing.setPropertyType("Entire guesthouse");
        listing.setRoomType("Entire home/apt");
        listing.setAccommodates(3);
        listing.setBathrooms(1.0d);
        listing.setBathroomsText("1 bath");
        listing.setBedrooms(1);
        listing.setBeds(2);
        listing.setPrice("$108.00");
        listing.setMinimumNights(2);
        listing.setMaximumNights(90);
        listing.setMinimumMinimumNights(2);
        listing.setMaximumMinimumNights(2);
        listing.setMaximumMaximumNights(90);
        listing.setMinimumNightsAvgNtm(2.0F);
        listing.setMaximumNightsAvgNtm(90F);
        listing.setCalendarUpdated(null);
        listing.setHasAvailability(true);
        listing.setAvailability30(19);
        listing.setAvailability60(44);
        listing.setAvailability90(71);
        listing.setAvailability365(329);
        listing.setCalendarLastScraped(listingsEtl.convertDate("2024-06-18"));
        listing.setNumberOfReviews(385);
        listing.setNumberOfReviewsLtm(37);
        listing.setNumberOfReviewsL30d(3);
        listing.setFirstReview(listingsEtl.convertDate("2009-03-08"));
        listing.setLastReview(listingsEtl.convertDate("2024-06-06"));
        listing.setReviewScoresRating(4.85F);
        listing.setReviewScoresAccuracy(4.88F);
        listing.setReviewScoresCleanliness(4.86F);
        listing.setReviewScoresCheckin(4.9F);
        listing.setReviewScoresLocation(4.83F);
        listing.setReviewScoresValue(4.79F);
        listing.setLicense(null);
        listing.setInstantBookable(false);
        listing.setCalculatedHostListingsCount(1);
        listing.setCalculatedHostListingsCountEntireHomes(1);
        listing.setCalculatedHostListingsCountPrivateRooms(0);
        listing.setCalculatedHostListingsCountSharedRooms(0);
        listing.setReviewsPerMonth(3.68F);
        Listing result = listingController.insertListing(listing);
        assertNotNull(result, "Expected a listing but found none.");
        assertNotNull(listingController.findById(result.getId()), "Expected to find Listing by ID. but found none.");
        assertEquals(20240617171903L, listing.getScrapeId());

    }

}
