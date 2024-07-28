package com.byteworksinc.airbnb.dao.impl;

import com.byteworksinc.airbnb.dao.ListingDao;
import com.byteworksinc.airbnb.entities.Listing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class ListingDaoJDBCTemplateImpl implements ListingDao {

    private final DataSource dataSource;

    public ListingDaoJDBCTemplateImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @Transactional
    public Listing save(Listing listing) {
        // 79 column
        String query = """
                insert into listing (
                id, listing_url, scrape_id, source, last_scraped, name, description, neighborhood_overview, picture_url,
                host_id, host_url, host_name, host_since, host_location, host_about, host_response_time, host_response_rate, host_acceptance_rate, host_is_superhost,
                host_thumbnail_url, host_picture_url, host_neighbourhood, host_listings_count, host_total_listings_count, host_verifications, host_has_profile_pic, host_identity_verified, neighbourhood, neighbourhood_cleansed, neighbourhood_group_cleansed, latitude,
                longitude, property_type, room_type, accommodates, bathrooms, bathrooms_text, bedrooms, beds, price, minimum_nights,
                minimum_minimum_nights, maximum_nights, maximum_minimum_nights, maximum_maximum_nights, minimum_maximum_nights, minimum_nights_avg_ntm, calendar_updated, has_availability, availability_30, availability_60,
                availability_90, availability_365, calendar_last_scraped, number_of_reviews, number_of_reviews_ltm, number_of_reviews_l30d, first_review, last_review, review_scores_rating, review_scores_accuracy,
                review_scores_cleanliness, review_scores_checkin, review_scores_communication, review_scores_location, review_scores_value, license, instant_bookable, calculated_host_listings_count, calculated_host_listings_count_entire_homes,
                calculated_host_listings_count_private_rooms, calculated_host_listings_count_shared_rooms, reviews_per_month
                )
                values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Object[] args = ListingObjectBuilder.build(listing);

        int out = jdbcTemplate.update(query, args);
        if (out == 0) {
            log.error("Listing save failed with id=" + listing.id());
        }
        return findById(listing.id());
    }


    @Override
    public List<Listing> findAll() {
        String query = "SELECT * FROM listing";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(query, new ListingRowMapper());
    }

    @Override
    public Listing findById(Long id) {
        String query = "SELECT * FROM listing WHERE id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(query, new ListingRowMapper(), id);
    }

    @Override
    public void deleteListing(Long id) {
        String query = "DELETE FROM listing WHERE id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("TRUNCATE TABLE listing");
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not save listing.", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    con.close();
                }
            } catch (SQLException e) {
                log.error("Could not close prepared statement or connection.", e);
            }
        }
    }

}
