package com.byteworksinc.airbnb.etl;

import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.repositories.ListingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ListingsEtl {
    private final ListingRepository listingRepository;

    private final String[] headers = {"id", "listing_url", "scrape_id", "last_scraped", "source", "name", "description",
            "neighborhood_overview", "picture_url", "host_id", "host_url", "host_name", "host_since", "host_location",
            "host_about", "host_response_time", "host_response_rate", "host_acceptance_rate", "host_is_superhost",
            "host_thumbnail_url", "host_picture_url", "host_neighbourhood", "host_listings_count", "host_total_listings_count",
            "host_verifications", "host_has_profile_pic", "host_identity_verified", "neighbourhood", "neighbourhood_cleansed",
            "neighbourhood_group_cleansed", "latitude", "longitude", "property_type", "room_type", "accommodates", "bathrooms",
            "bathrooms_text", "bedrooms", "beds", "amenities", "price", "minimum_nights", "maximum_nights", "minimum_minimum_nights",
            "maximum_minimum_nights", "minimum_maximum_nights", "maximum_maximum_nights", "minimum_nights_avg_ntm",
            "maximum_nights_avg_ntm", "calendar_updated", "has_availability", "availability_30", "availability_60", "availability_90",
            "availability_365", "calendar_last_scraped", "number_of_reviews", "number_of_reviews_ltm", "number_of_reviews_l30d",
            "first_review", "last_review", "review_scores_rating", "review_scores_accuracy", "review_scores_cleanliness",
            "review_scores_checkin", "review_scores_communication", "review_scores_location", "review_scores_value", "license",
            "instant_bookable", "calculated_host_listings_count", "calculated_host_listings_count_entire_homes",
            "calculated_host_listings_count_private_rooms", "calculated_host_listings_count_shared_rooms", "reviews_per_month" };

    public ListingsEtl(final ListingRepository listingRepository, @Value("${airbnbLoadListings:false}") final boolean loadListings) {
        this.listingRepository = listingRepository;
        if (loadListings) {
            listingRepository.deleteAll();
            readListingCsvFile("data/listings.csv");
        }
        log.info(String.format("loadListings is %s", loadListings));
    }

    public void readListingCsvFile(final String path) {
        InputStream stream = null;
        try {
            stream = new ClassPathResource(path).getInputStream();
            readListingInputStream(stream);
        } catch (Exception e) {
            log.error("Error loading data", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.error("Could not close listings CSV", e);
                }
            }
        }
    }

    private void readListingInputStream(InputStream input) throws Exception {
        int count = 0;
        int total = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Listing> listings = new ArrayList<>();
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(headers)
                .withFirstRecordAsHeader()
                .parse(reader);
        for (CSVRecord record : records) {
            count += 1;
            total += 1;
            Listing listing = new Listing();
            listing.setId(Long.parseLong(record.get("id")));
            listing.setScrapeId(getLong(record, "scrape_id"));
            listing.setSource(record.get("source"));
            listing.setName(record.get("name"));
            listing.setDescription(record.get("description"));
            listing.setNeighborhoodOverview(record.get("neighborhood_overview"));
            listing.setPictureUrl(record.get("picture_url"));
            listing.setHostId(Long.parseLong(record.get("host_id")));
            listing.setHostUrl(record.get("host_url"));
            listing.setHostName(record.get("host_name"));
//                listing.setHostSince(record.get("host_since"));
            listing.setHostLocation(record.get("host_location"));
            listing.setHostAbout(record.get("host_about"));
            listing.setHostResponseTime(record.get("host_response_time"));
            listing.setHostResponseRate(record.get("host_response_rate"));
            listing.setHostAcceptanceRate(record.get("host_acceptance_rate"));
            listing.setHostIsSuperHost(Boolean.parseBoolean(record.get("host_is_superhost")));
            listing.setHostThumbnailUrl(record.get("host_thumbnail_url"));
            listing.setHostPictureUrl(record.get("host_picture_url"));
            listing.setHostNeighbourhood(record.get("host_neighbourhood"));
            listing.setHostListingsCount(getInteger(record, "host_listings_count"));
            listing.setHostTotalListingsCount(getInteger(record, "host_total_listings_count"));
            listing.setHostVerifications("".concat(record.get("host_verifications")));
            listing.setHostHasProfilePic(Boolean.parseBoolean(record.get("host_has_profile_pic")));
            listing.setHostIdentityVerified(Boolean.parseBoolean(record.get("host_identity_verified")));
            listing.setNeighbourhood(record.get("neighbourhood"));
            listing.setNeighbourhoodCleansed(Boolean.parseBoolean(record.get("neighbourhood_group_cleansed")));
            listing.setLatitude(getBigDecimal(record, "latitude"));
            listing.setLongitude(getBigDecimal(record, "longitude"));
            listing.setPropertyType(record.get("property_type"));
            listing.setRoomType(record.get("room_type"));
            listing.setAccommodates(getInteger(record, "accommodates"));
            listing.setBathrooms(getDouble(record, "bathrooms"));
            listing.setBathroomsText(record.get("bathrooms_text"));
            listing.setBeds(getInteger(record, "beds"));
            listing.setPrice(record.get("price"));
            listing.setMinimumNights(getInteger(record, "minimum_nights"));
            listing.setMaximumNights(getInteger(record, "maximum_nights"));
            listing.setMinimumMinimumNights(getInteger(record, "minimum_minimum_nights"));
            listing.setMinimumMaximumNights(getInteger(record, "minimum_maximum_nights"));
            listing.setMaximumMinimumNights(getInteger(record, "maximum_minimum_nights"));
            listing.setMaximumMaximumNights(getInteger(record, "maximum_maximum_nights"));
            listing.setMaximumNightsAvgNtm(getFloat(record, "minimum_nights_avg_ntm"));
            listing.setCalendarUpdated(record.get("calendar_updated"));
            listing.setHasAvailability(Boolean.parseBoolean(record.get("has_availability")));
            listing.setAvailability30(getInteger(record, "availability_30"));
            listing.setAvailability60(getInteger(record, "availability_60"));
            listing.setAvailability365(getInteger(record, "availability_365"));
//                listing.setCalendarLastScraped(getInteger(record, "calendar_last_scraped"));
            listing.setNumberOfReviews(getInteger(record, "number_of_reviews"));
            listing.setNumberOfReviewsLtm(getInteger(record, "number_of_reviews_ltm"));
            listing.setNumberOfReviewsL30d(getInteger(record, "number_of_reviews_l30d"));
//                listing.setFirstReview(record.get("first_review"));
//                listing.setFirstReview(record.get("last_review"));
            listing.setReviewScoresRating(getFloat(record, "review_scores_rating"));
            listing.setReviewScoresAccuracy(getFloat(record, "review_scores_accuracy"));
            listing.setReviewScoresAccuracy(getFloat(record, "review_scores_accuracy"));
            listing.setReviewScoresCleanliness(getFloat(record, "review_scores_cleanliness"));
            listing.setReviewScoresCheckin(getFloat(record, "review_scores_checkin"));
            listing.setReviewScoresCommunication(getFloat(record, "review_scores_communication"));
            listing.setReviewScoresLocation(getFloat(record, "review_scores_location"));
            listing.setReviewScoresValue(getFloat(record, "review_scores_value"));
            listing.setLicense(record.get("license"));
            listing.setInstantBookable(Boolean.parseBoolean(record.get("instant_bookable")));
            listing.setCalculatedHostListingsCount(getInteger(record, "calculated_host_listings_count"));
            listing.setCalculatedHostListingsCountEntireHomes(getInteger(record, "calculated_host_listings_count_entire_homes"));
            listing.setCalculatedHostListingsCountPrivateRooms(getInteger(record, "calculated_host_listings_count_private_rooms"));
            listing.setCalculatedHostListingsCountSharedRooms(getInteger(record, "calculated_host_listings_count_shared_rooms"));
            listing.setReviewsPerMonth(getFloat(record, "reviews_per_month"));
            listings.add(listing);

            if (count % 100 == 0) {
                log.info(String.format("Writing listing %s", total));
            }
            if (count > 500) {
                listingRepository.saveAllAndFlush(listings);
                listings.clear();
                count = 0;
            }
        }
        if (!listings.isEmpty()) {
            listingRepository.saveAllAndFlush(listings);
        }
        log.info(String.format("Wrote %s listings.", total));
    }

    public Integer getInteger(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                return Integer.parseInt(value);
            }
        }
        return null;
    }

    public Long getLong(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                return Long.parseLong(value);
            }
        }
        return null;
    }

    public Float getFloat(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                return Float.parseFloat(value);
            }
        }
        return null;
    }

    public Double getDouble(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                return Double.parseDouble(value);
            }
        }
        return null;
    }

    public BigDecimal getBigDecimal(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                value = value.replace("$", "");
                value = value.replace(",", "");
                double d = Double.parseDouble(value);
                return BigDecimal.valueOf(d);
            }
        }
        return null;
    }
}
