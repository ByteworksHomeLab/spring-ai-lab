package com.byteworksinc.airbnb.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@ToString
@NoArgsConstructor
@Table(name = "listing", schema = "public", indexes = {
        @Index(name = "IDXAirbnb_02", columnList = "hostId"),
        @Index(name = "IDXAirbnb_03", columnList = "propertyType"),
        @Index(name = "IDXAirbnb_04", columnList = "roomType"),
        @Index(name = "IDXAirbnb_05", columnList = "bedrooms")
})
public class Listing implements Serializable {
    @Id
    private Long id;
    private String listingUrl;
    private Long scrapeId;
    private String source;
    private Date lastSearched;
    private Date lastScraped;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String neighborhoodOverview;
    private String pictureUrl;
    private Long hostId;
    private String hostUrl;
    private String hostName;
    private Date hostSince;
    private String hostLocation;
    @Column(columnDefinition = "TEXT")
    private String hostAbout;
    private String hostResponseTime;
    private String hostResponseRate;
    private String hostAcceptanceRate;
    private boolean hostIsSuperHost;
    private String hostThumbnailUrl;
    private String hostPictureUrl;
    private String hostNeighbourhood;
    private Integer hostListingsCount;
    private Integer hostTotalListingsCount;
    private String hostVerifications;
    private boolean hostHasProfilePic;
    private boolean hostIdentityVerified;
    private String neighbourhood;
    private boolean neighbourhoodCleansed;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String propertyType;
    private String roomType;
    private Integer accommodates;
    private Double bathrooms;
    private String bathroomsText;
    private Integer bedrooms;
    private Integer beds;
    private String price;
    private Integer minimumNights;
    private Integer maximumNights;
    private Integer minimumMinimumNights;
    private Integer maximumMinimumNights;
    private Integer minimumMaximumNights;
    private Integer maximumMaximumNights;
    private Float minimumNightsAvgNtm;
    private Float maximumNightsAvgNtm;
    private String calendarUpdated;
    private boolean hasAvailability;
    private Integer availability30;
    private Integer availability60;
    private Integer availability90;
    private Integer availability365;
    private Date calendarLastScraped;
    private Integer numberOfReviews;
    private Integer numberOfReviewsLtm;
    private Integer numberOfReviewsL30d;
    private Date firstReview;
    private Date lastReview;
    private Float reviewScoresRating;
    private Float reviewScoresAccuracy;
    private Float reviewScoresCleanliness;
    private Float reviewScoresCheckin;
    private Float reviewScoresCommunication;
    private Float reviewScoresLocation;
    private Float reviewScoresValue;
    private boolean requiresLicense;
    private String license;
    private boolean instantBookable;
    private Integer calculatedHostListingsCount;
    private Integer calculatedHostListingsCountEntireHomes;
    private Integer calculatedHostListingsCountPrivateRooms;
    private Integer calculatedHostListingsCountSharedRooms;
    private Integer regionId;
    private String regionName;
    private Integer regionParentId;
    private String regionParentName;
    private Integer regionParentParentId;
    private String regionParentParentName;
    private Float reviewsPerMonth;
}
