package com.byteworksinc.airbnb.dao.impl;

import com.byteworksinc.airbnb.entities.Listing;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ListingRowMapper implements RowMapper<Listing> {
    @Override
    public Listing mapRow(ResultSet rs, int rowNum) throws SQLException {
        Listing listing = new Listing();

        listing.setId(rs.getLong("id"));
        listing.setListingUrl(rs.getString("listing_url"));
        listing.setScrapeId(rs.getLong("scrape_id"));
        listing.setSource(rs.getString("source"));
        listing.setLastSearched(rs.getDate("last_searched"));
        listing.setLastScraped(rs.getDate("last_scraped"));
        listing.setName(rs.getString("name"));
        listing.setDescription(rs.getString("description"));
        listing.setNeighborhoodOverview(rs.getString("neighborhood_overview"));
        listing.setPictureUrl(rs.getString("picture_url"));
        listing.setHostId(rs.getLong("host_id"));
        listing.setHostUrl(rs.getString("host_url"));
        listing.setHostName(rs.getString("host_name"));
        listing.setHostSince(rs.getDate("host_since"));
        listing.setHostLocation(rs.getString("host_location"));
        listing.setHostAbout(rs.getString("host_about"));
        listing.setHostResponseTime(rs.getString("host_response_time"));
        listing.setHostResponseRate(rs.getString("host_response_rate"));
        listing.setHostAcceptanceRate(rs.getString("host_acceptance_rate"));
        listing.setHostIsSuperhost(rs.getBoolean("host_is_superhost"));
        listing.setHostThumbnailUrl(rs.getString("host_thumbnail_url"));
        listing.setHostPictureUrl(rs.getString("host_picture_url"));
        listing.setHostNeighbourhood(rs.getString("host_neighbourhood"));
        listing.setHostListingsCount(rs.getInt("host_listings_count"));
        listing.setHostTotalListingsCount(rs.getInt("host_total_listings_count"));
        listing.setHostVerifications(rs.getString("host_verifications"));
        listing.setHostHasProfilePic(rs.getBoolean("host_has_profile_pic"));
        listing.setHostIdentityVerified(rs.getBoolean("host_identity_verified"));
        listing.setNeighbourhood(rs.getString("neighbourhood"));
        listing.setLatitude(rs.getBigDecimal("latitude"));
        listing.setLongitude(rs.getBigDecimal("longitude"));
        listing.setPropertyType(rs.getString("property_type"));
        listing.setRoomType(rs.getString("room_type"));
        listing.setAccommodates(rs.getInt("accommodates"));
        listing.setBathrooms(rs.getDouble("bathrooms"));
        listing.setBathroomsText(rs.getString("bathrooms_text"));
        listing.setBedrooms(rs.getInt("bedrooms"));
        listing.setBeds(rs.getInt("beds"));
        listing.setPrice(rs.getString("price"));
        listing.setMinimumMinimumNights(rs.getInt("minimum_minimum_nights"));
        listing.setMaximumNights(rs.getInt("maximum_nights"));
        listing.setMaximumMinimumNights(rs.getInt("maximum_minimum_nights"));
        listing.setMaximumMaximumNights(rs.getInt("maximum_maximum_nights"));
        listing.setMinimumMaximumNights(rs.getInt("minimum_maximum_nights"));
        listing.setMinimumNightsAvgNtm(rs.getFloat("minimum_nights_avg_ntm"));
        listing.setCalendarUpdated(rs.getString("calendar_updated"));
        listing.setHasAvailability(rs.getBoolean("has_availability"));
        listing.setAvailability30(rs.getInt("availability_30"));
        listing.setAvailability60(rs.getInt("availability_60"));
        listing.setAvailability90(rs.getInt("availability_90"));
        listing.setAvailability365(rs.getInt("availability_365"));
        listing.setCalendarLastScraped(rs.getDate("calendar_last_scraped"));
        listing.setNumberOfReviews(rs.getInt("number_of_reviews"));
        listing.setNumberOfReviewsLtm(rs.getInt("number_of_reviews_ltm"));
        listing.setNumberOfReviewsL30d(rs.getInt("number_of_reviews_l30d"));
        listing.setFirstReview(rs.getDate("first_review"));
        listing.setLastReview(rs.getDate("last_review"));
        listing.setReviewScoresRating(rs.getFloat("review_scores_accuracy"));
        listing.setReviewScoresAccuracy(rs.getFloat("review_scores_accuracy"));
        listing.setReviewScoresCleanliness(rs.getFloat("review_scores_cleanliness"));
        listing.setReviewScoresCheckin(rs.getFloat("review_scores_checkin"));
        listing.setReviewScoresCommunication(rs.getFloat("review_scores_communication"));
        listing.setReviewScoresLocation(rs.getFloat("review_scores_location"));
        listing.setReviewScoresLocation(rs.getFloat("review_scores_value"));
        listing.setReviewScoresValue(rs.getFloat("review_scores_value"));
        listing.setRequiresLicense(rs.getBoolean("requires_license"));
        listing.setLicense(rs.getString("license"));
        listing.setInstantBookable(rs.getBoolean("instant_bookable"));
        listing.setCalculatedHostListingsCount(rs.getInt("calculated_host_listings_count"));
        listing.setCalculatedHostListingsCountEntireHomes(rs.getInt("calculated_host_listings_count_entire_homes"));
        listing.setCalculatedHostListingsCountPrivateRooms(rs.getInt("calculated_host_listings_count_private_rooms"));
        listing.setCalculatedHostListingsCountSharedRooms(rs.getInt("calculated_host_listings_count_shared_rooms"));
        listing.setRegionId(rs.getLong("region_id"));
        listing.setRegionName(rs.getString("region_name"));
        listing.setRegionParentId(rs.getLong("region_parent_id"));
        listing.setRegionParentName(rs.getString("region_parent_name"));
        listing.setRegionParentParentId(rs.getLong("region_parent_parent_id"));
        listing.setRegionParentParentName(rs.getString("region_parent_parent_name"));
        listing.setReviewsPerMonth(rs.getFloat("reviews_per_month"));

        return listing;
    }

}
