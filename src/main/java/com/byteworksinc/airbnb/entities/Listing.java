package com.byteworksinc.airbnb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;


public record Listing(@Id Long id, String listingUrl, @NotEmpty Long scrapeId, String source, @NotEmpty Date lastScraped,
                      String name, String description, String neighborhoodOverview, String pictureUrl, @NotEmpty Long hostId,
                      @NotEmpty String hostUrl, @NotEmpty String hostName, Date hostSince, String hostLocation, String hostAbout,
                      @NotEmpty String hostResponseTime, @NotEmpty String hostResponseRate, @NotEmpty String hostAcceptanceRate,
                      boolean hostIsSuperhost, @NotEmpty String hostThumbnailUrl, @NotEmpty String hostPictureUrl, @NotEmpty String hostNeighbourhood,
                      Integer hostListingsCount, Integer hostTotalListingsCount, @NotEmpty String hostVerifications,
                      boolean hostHasProfilePic, boolean hostIdentityVerified, String neighbourhood,
                      boolean neighbourhoodCleansed, boolean neighbourhoodGroupCleansed, @NotEmpty BigDecimal latitude,
                      @NotEmpty BigDecimal longitude, String propertyType, String roomType, Integer accommodates,
                      Double bathrooms, String bathroomsText, Integer bedrooms, Integer beds, @NotEmpty String price,
                      @NotEmpty Integer minimumNights, @NotEmpty Integer maximumNights, Integer minimumMinimumNights,
                      Integer maximumMinimumNights, Integer minimumMaximumNights, Integer maximumMaximumNights,
                      Float minimumNightsAvgNtm, Float maximumNightsAvgNtm, String calendarUpdated,
                      boolean hasAvailability, Integer availability30, Integer availability60, Integer availability90,
                      Integer availability365, Date calendarLastScraped, @NotEmpty Integer numberOfReviews,
                      @NotEmpty Integer numberOfReviewsLtm, Integer numberOfReviewsL30d, Date firstReview, Date lastReview,
                      BigDecimal reviewScoresRating, BigDecimal reviewScoresAccuracy,
                      BigDecimal reviewScoresCleanliness, BigDecimal reviewScoresCheckin,
                      BigDecimal reviewScoresCommunication, BigDecimal reviewScoresLocation,
                      BigDecimal reviewScoresValue, String license, boolean instantBookable,
                      @NotEmpty Integer calculatedHostListingsCount, @NotEmpty Integer calculatedHostListingsCountEntireHomes,
                      @NotEmpty Integer calculatedHostListingsCountPrivateRooms, @NotEmpty Integer calculatedHostListingsCountSharedRooms,
                      BigDecimal reviewsPerMonth,
                      @Version
                      Integer version
        ) {

}
        

