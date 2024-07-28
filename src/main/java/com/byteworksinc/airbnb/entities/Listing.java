package com.byteworksinc.airbnb.entities;

import java.math.BigDecimal;
import java.util.Date;


public record Listing(long id, String listingUrl, long scrapeId, String source, Date lastScraped,
                      String name, String description, String neighborhoodOverview, String pictureUrl, long hostId,
                      String hostUrl, String hostName, Date hostSince, String hostLocation, String hostAbout,
                      String hostResponseTime, String hostResponseRate, String hostAcceptanceRate,
                      boolean hostIsSuperhost, String hostThumbnailUrl, String hostPictureUrl, String hostNeighbourhood,
                      Integer hostListingsCount, Integer hostTotalListingsCount, String hostVerifications,
                      boolean hostHasProfilePic, boolean hostIdentityVerified, String neighbourhood,
                      boolean neighbourhoodCleansed, boolean neighbourhoodGroupCleansed, BigDecimal latitude,
                      BigDecimal longitude, String propertyType, String roomType, Integer accommodates,
                      Double bathrooms, String bathroomsText, Integer bedrooms, Integer beds, String price,
                      Integer minimumNights, Integer maximumNights, Integer minimumMinimumNights,
                      Integer maximumMinimumNights, Integer minimumMaximumNights, Integer maximumMaximumNights,
                      Float minimumNightsAvgNtm, Float maximumNightsAvgNtm, String calendarUpdated,
                      boolean hasAvailability, Integer availability30, Integer availability60, Integer availability90,
                      Integer availability365, Date calendarLastScraped, Integer numberOfReviews,
                      Integer numberOfReviewsLtm, Integer numberOfReviewsL30d, Date firstReview, Date lastReview,
                      BigDecimal reviewScoresRating, BigDecimal reviewScoresAccuracy,
                      BigDecimal reviewScoresCleanliness, BigDecimal reviewScoresCheckin,
                      BigDecimal reviewScoresCommunication, BigDecimal reviewScoresLocation,
                      BigDecimal reviewScoresValue, String license, boolean instantBookable,
                      Integer calculatedHostListingsCount, Integer calculatedHostListingsCountEntireHomes,
                      Integer calculatedHostListingsCountPrivateRooms, Integer calculatedHostListingsCountSharedRooms,
                      BigDecimal reviewsPerMonth) {

}
        

