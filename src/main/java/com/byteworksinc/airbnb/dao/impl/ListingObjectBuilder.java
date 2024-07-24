package com.byteworksinc.airbnb.dao.impl;

import com.byteworksinc.airbnb.entities.Listing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ListingObjectBuilder {

    public static Object[] build(Listing listing) {

        return new Object[]{
                listing.getId(),
                listing.getListingUrl(),
                listing.getScrapeId(),
                listing.getSource(),
                listing.getLastSearched(),
                listing.getLastScraped(),
                listing.getName(),
                listing.getDescription(),
                listing.getNeighborhoodOverview(),
                listing.getPictureUrl(),
                listing.getHostId(),
                listing.getHostUrl(),
                listing.getHostName(),
                listing.getHostSince(),
                listing.getHostLocation(),
                listing.getHostAbout(),
                listing.getHostResponseTime(),
                listing.getHostResponseRate(),
                listing.getHostAcceptanceRate(),
                listing.isHostIsSuperhost(),
                listing.getHostThumbnailUrl(),
                listing.getHostPictureUrl(),
                listing.getHostNeighbourhood(),
                listing.getHostListingsCount(),
                listing.getHostTotalListingsCount(),
                listing.getHostVerifications(),
                listing.isHostHasProfilePic(),
                listing.isHostIdentityVerified(),
                listing.getNeighbourhood(),
                listing.getLatitude(),
                listing.getLongitude(),
                listing.getPropertyType(),
                listing.getRoomType(),
                listing.getAccommodates(),
                listing.getBathrooms(),
                listing.getBathroomsText(),
                listing.getBedrooms(),
                listing.getBeds(),
                listing.getPrice(),
                listing.getMinimumNights(),
                listing.getMinimumMinimumNights(),
                listing.getMaximumNights(),
                listing.getMaximumMinimumNights(),
                listing.getMaximumMaximumNights(),
                listing.getMinimumMaximumNights(),
                listing.getMinimumNightsAvgNtm(),
                listing.getCalendarUpdated(),
                listing.isHasAvailability(),
                listing.getAvailability30(),
                listing.getAvailability60(),
                listing.getAvailability90(),
                listing.getAvailability365(),
                listing.getCalendarLastScraped(),
                listing.getNumberOfReviews(),
                listing.getNumberOfReviewsLtm(),
                listing.getNumberOfReviewsL30d(),
                listing.getFirstReview(),
                listing.getLastReview(),
                listing.getReviewScoresRating(),
                listing.getReviewScoresAccuracy(),
                listing.getReviewScoresCleanliness(),
                listing.getReviewScoresCheckin(),
                listing.getReviewScoresCommunication(),
                listing.getReviewScoresLocation(),
                listing.getReviewScoresValue(),
                listing.isRequiresLicense(),
                listing.getLicense(),
                listing.isInstantBookable(),
                listing.getCalculatedHostListingsCount(),
                listing.getCalculatedHostListingsCountEntireHomes(),
                listing.getCalculatedHostListingsCountPrivateRooms(),
                listing.getCalculatedHostListingsCountSharedRooms(),
                listing.getRegionId(),
                listing.getRegionName(),
                listing.getRegionParentId(),
                listing.getRegionParentName(),
                listing.getRegionParentParentId(),
                listing.getRegionParentParentName(),
                listing.getReviewsPerMonth()
        };
    }
}
