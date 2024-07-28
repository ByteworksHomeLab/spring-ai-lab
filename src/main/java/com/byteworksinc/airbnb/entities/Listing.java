package com.byteworksinc.airbnb.entities;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;


public record Listing(@Id Long id, String listingUrl, Long scrapeId, String source, Date lastScraped,
                      String name, String description, String neighborhoodOverview, String pictureUrl, Long hostId,
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

    public Listing {
        if (id == null) {
            throw new IllegalArgumentException("Listing ID must not be null.");
        }
        if (scrapeId == null) {
            throw new IllegalArgumentException("Listing Scrape ID must not be null.");
        }
        if (lastScraped == null) {
            throw new IllegalArgumentException("Listing lastScraped must not be null.");
        }
        if (hostId == null) {
            throw new IllegalArgumentException("Listing hostId must not be null.");
        }
        if (hostUrl == null) {
            throw new IllegalArgumentException("Listing hostUrl must not be null.");
        }
        if (hostName == null) {
            throw new IllegalArgumentException("Listing hostName must not be null.");
        }
        if (hostResponseTime == null) {
            throw new IllegalArgumentException("Listing hostResponseTime must not be null.");
        }
        if (hostResponseRate == null) {
            throw new IllegalArgumentException("Listing hostResponseRate must not be null.");
        }
        if (hostAcceptanceRate == null) {
            throw new IllegalArgumentException("Listing hostAcceptanceRate must not be null.");
        }
        if (hostThumbnailUrl == null) {
            throw new IllegalArgumentException("Listing hostThumbnailUrl must not be null.");
        }
        if (hostPictureUrl == null) {
            throw new IllegalArgumentException("Listing hostPictureUrl must not be null.");
        }
        if (hostVerifications == null) {
            throw new IllegalArgumentException("Listing hostVerifications must not be null.");
        }
        if (hostNeighbourhood == null) {
            throw new IllegalArgumentException("Listing hostNeighbourhood must not be null.");
        }
        if (latitude == null) {
            throw new IllegalArgumentException("latitude hostNeighbourhood must not be null.");
        }
        if (longitude == null) {
            throw new IllegalArgumentException("longitude hostNeighbourhood must not be null.");
        }
        if (price == null) {
            throw new IllegalArgumentException("longitude price must not be null.");
        }
        if (minimumNights == null) {
            throw new IllegalArgumentException("longitude minimumNights must not be null.");
        }
        if (maximumNights == null) {
            throw new IllegalArgumentException("longitude maximumNights must not be null.");
        }
        if (numberOfReviews == null) {
            throw new IllegalArgumentException("longitude numberOfReviews must not be null.");
        }
        if (numberOfReviewsLtm == null) {
            throw new IllegalArgumentException("longitude numberOfReviewsLtm must not be null.");
        }
        if (calculatedHostListingsCount == null) {
            throw new IllegalArgumentException("longitude calculatedHostListingsCount must not be null.");
        }
        if (calculatedHostListingsCountEntireHomes == null) {
            throw new IllegalArgumentException("longitude calculatedHostListingsCountEntireHomes must not be null.");
        }
        if (calculatedHostListingsCountPrivateRooms == null) {
            throw new IllegalArgumentException("longitude calculatedHostListingsCountPrivateRooms must not be null.");
        }
        if (calculatedHostListingsCountSharedRooms == null) {
            throw new IllegalArgumentException("longitude calculatedHostListingsCountSharedRooms must not be null.");
        }
    }

}
        

