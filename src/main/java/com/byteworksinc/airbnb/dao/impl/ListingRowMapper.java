package com.byteworksinc.airbnb.dao.impl;

import com.byteworksinc.airbnb.entities.Listing;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ListingRowMapper implements RowMapper<Listing> {
    @Override
    public Listing mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Listing(rs.getLong("id"), rs.getString("listing_url"), rs.getLong("scrape_id"), rs.getString("source"), rs.getDate("last_scraped"), rs.getString("name"), rs.getString("description"), rs.getString("neighborhood_overview"), rs.getString("picture_url"), rs.getLong("host_id"), rs.getString("host_url"), rs.getString("host_name"), rs.getDate("host_since"), rs.getString("host_location"), rs.getString("host_about"), rs.getString("host_response_time"), rs.getString("host_response_rate"), rs.getString("host_acceptance_rate"), rs.getBoolean("host_is_superhost"), rs.getString("host_thumbnail_url"), rs.getString("host_picture_url"), rs.getString("host_neighbourhood"), rs.getInt("host_listings_count"), rs.getInt("host_total_listings_count"), rs.getString("host_verifications"), rs.getBoolean("host_has_profile_pic"), rs.getBoolean("host_identity_verified"), rs.getString("neighbourhood"), rs.getBoolean("neighbourhood_cleansed"), rs.getBoolean("neighbourhood_group_cleansed"), rs.getBigDecimal("latitude"), rs.getBigDecimal("longitude"), rs.getString("property_type"), rs.getString("room_type"), rs.getInt("accommodates"), rs.getDouble("bathrooms"), rs.getString("bathrooms_text"), rs.getInt("bedrooms"), rs.getInt("beds"), rs.getString("price"), rs.getInt("minimum_nights"), rs.getInt("maximum_nights"), rs.getInt("minimum_minimum_nights"), rs.getInt("maximum_minimum_nights"), rs.getInt("minimum_maximum_nights"), rs.getInt("maximum_maximum_nights"), rs.getFloat("minimum_nights_avg_ntm"), rs.getFloat("maximum_nights_avg_ntm"), rs.getString("calendar_updated"), rs.getBoolean("has_availability"), rs.getInt("availability_30"), rs.getInt("availability_60"), rs.getInt("availability_90"), rs.getInt("availability_365"), rs.getDate("calendar_last_scraped"), rs.getInt("number_of_reviews"), rs.getInt("number_of_reviews_ltm"), rs.getInt("number_of_reviews_l30d"), rs.getDate("first_review"), rs.getDate("last_review"), rs.getBigDecimal("review_scores_rating"), rs.getBigDecimal("review_scores_accuracy"), rs.getBigDecimal("review_scores_cleanliness"), rs.getBigDecimal("review_scores_checkin"), rs.getBigDecimal("review_scores_communication"), rs.getBigDecimal("review_scores_location"), rs.getBigDecimal("review_scores_value"), rs.getString("license"), rs.getBoolean("instant_bookable"), rs.getInt("calculated_host_listings_count"), rs.getInt("calculated_host_listings_count_entire_homes"), rs.getInt("calculated_host_listings_count_private_rooms"), rs.getInt("calculated_host_listings_count_shared_rooms"), rs.getBigDecimal("reviews_per_month"));
    }

}
