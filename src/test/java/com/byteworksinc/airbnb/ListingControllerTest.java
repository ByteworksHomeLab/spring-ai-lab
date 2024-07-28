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
        Listing listing = new Listing(1234567L, "https://www.airbnb.com/rooms/5456", 20240617171903L, "city scrape", listingsEtl.convertDate("2024-06-18"), "Walk to 6th, Rainey St and Convention Ctr", "Great central  location for walking to Convention Center, Rainey Street, East 6th Street, Downtown, Congress Ave Bats.<br /><br />  Free wifi<br /><br />No Smoking,  No pets'",  "My neighborhood is ideally located if you want to walk to bars and restaurants downtown, East 6th Street or Rainey Street.  The Convention Center is only 3 1/2 blocks away and a quick 10 minute walk. Whole foods store located 5 blks , easily walkable.", "https://a0.muscache.com/pictures/14084884/b5a35a84_original.jpg", 8028L, "https://www.airbnb.com/users/show/8028", "Sylvia", listingsEtl.convertDate("2009-02-16"), "Austin, TX", "I am a licensed Real Estate Broker and owner of Armadillo Realty.  I attended The University of Texas at Austin and fell in love with the small town that it was back in 1979; I have been here every since.  I love the Art, Music and Film scene here in Austin.  There is so much natural beauty to enjoy as well. I especially enjoy Barton Springs Pool in the summertime along with the Zilker Hillside theater productions. SXSW, Austin City Limits Festival and the East Austin Art Studio Tour are among my favorite events. I also enjoy a sunset cruise on my canoe to Congress bridge to see the Mexican Freetail Bats come out for their nightly feeding.  ", "within an hour", "100%", "100%", false, "https://a0.muscache.com/im/users/8028/profile_pic/1329882962/original.jpg?aki_policy=profile_small", "https://a0.muscache.com/im/users/8028/profile_pic/1329882962/original.jpg?aki_policy=profile_x_medium", "East Downtown", 1, 2, "['email', 'phone']", false, false, "Neighborhood highlights", false, false, BigDecimal.valueOf(30.26057).setScale(5, RoundingMode.HALF_UP), BigDecimal.valueOf(-97.73441).setScale(5, RoundingMode.HALF_UP), "Entire guesthouse", "Entire home/apt", 3, 1.0d, "1 bath", 1, 2, "$108.00", 2, 90, 2, 2, 90, 90, 2.0F, null, null, false, 19, 44, 71, 329, listingsEtl.convertDate("2024-06-18"), 685, 37, 3, listingsEtl.convertDate("2009-03-08"), listingsEtl.convertDate("2024-06-06"), BigDecimal.valueOf(4.85F),  BigDecimal.valueOf(4.88F),  BigDecimal.valueOf(4.86F),  BigDecimal.valueOf(4.9F),  BigDecimal.valueOf(4.83F),  BigDecimal.valueOf(4.79F),  BigDecimal.valueOf(4.9F), null, false, 1, 1, 0, 0,  BigDecimal.valueOf(3.68F));
        Listing result = listingController.insertListing(listing);
        assertNotNull(result, "Expected a listing but found none.");
        assertNotNull(listingController.findById(result.id()), "Expected to find Listing by ID. but found none.");
        assertEquals(20240617171903L, listing.scrapeId());

    }

}
