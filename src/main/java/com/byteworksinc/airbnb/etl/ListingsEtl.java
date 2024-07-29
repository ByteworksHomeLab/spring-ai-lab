package com.byteworksinc.airbnb.etl;

import com.byteworksinc.airbnb.dao.ListingRepository;
import com.byteworksinc.airbnb.entities.Listing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
@Component
public class ListingsEtl implements CommandLineRunner {
    private final ListingRepository listingRepository;
    private final boolean loadListings;
    private final boolean clearListingsTable;

    private final String[] headers = {"id", "listing_url", "scrape_id", "last_scraped", "source", "name", "description",
            "neighborhood_overview", "picture_url", "host_id", "host_url", "host_name", "host_since", "host_location",
            "host_about", "host_response_time", "host_response_rate", "host_acceptance_rate", "host_is_superhost",
            "host_thumbnail_url", "host_picture_url", "host_neighbourhood", "host_listings_count", "host_total_listings_count",
            "host_verifications", "host_has_profile_pic", "host_identity_verified", "neighbourhood", "neighbourhood_group_cleansed",
            "neighbourhood_group_cleansed", "latitude", "longitude", "property_type", "room_type", "accommodates", "bathrooms",
            "bathrooms_text", "bedrooms", "beds", "amenities", "price", "minimum_nights", "maximum_nights", "minimum_minimum_nights",
            "maximum_minimum_nights", "minimum_maximum_nights", "maximum_maximum_nights", "minimum_nights_avg_ntm",
            "maximum_nights_avg_ntm", "calendar_updated", "has_availability", "availability_30", "availability_60", "availability_90",
            "availability_365", "calendar_last_scraped", "number_of_reviews", "number_of_reviews_ltm", "number_of_reviews_l30d",
            "first_review", "last_review", "review_scores_rating", "review_scores_accuracy", "review_scores_cleanliness",
            "review_scores_checkin", "review_scores_communication", "review_scores_location", "review_scores_value", "license",
            "instant_bookable", "calculated_host_listings_count", "calculated_host_listings_count_entire_homes",
            "calculated_host_listings_count_private_rooms", "calculated_host_listings_count_shared_rooms", "reviews_per_month"};

    public ListingsEtl(final ListingRepository listingRepository, @Value("${airbnbLoadListings}") final boolean loadListings, @Value("${clearAirbnbListingsTable}") final boolean clearListingsTable) {
        this.listingRepository = listingRepository;
        this.loadListings = loadListings;
        this.clearListingsTable = clearListingsTable;
    }

    @Override
    public void run(String... args) {
        log.info(String.format("ListingsETL is starting. Listing records is %s", listingRepository.count()));
        if (clearListingsTable) {
            log.info("ListingsETL is clearing the Listings table");
            listingRepository.deleteAll();
        }
        // This isn't testing listingRepository.count() because it slowed down the unit tests.
        if (loadListings) {
            log.info("ListingsETL is loading the Listings table");
            readListingCsvFile("data/listings.csv");
        }
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
        Listing listing;
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(headers)
                .withFirstRecordAsHeader()
                .parse(reader);
        for (CSVRecord record : records) {
            count += 1;
            listing = new Listing(
                    getLong(record, "id"),
                    record.get("listing_url"),
                    getLong(record, "scrape_id"),
                    record.get("source"),
                    convertDate(record.get("last_scraped")),
                    record.get("name"),
                    record.get("description"),
                    record.get("neighborhood_overview"),
                    record.get("picture_url"),
                    getLong(record, "host_id"),
                    record.get("host_url"),
                    record.get("host_name"),
                    convertDate(record.get("host_since")),
                    record.get("host_location"),
                    record.get("host_about"),
                    record.get("host_response_time"),
                    record.get("host_response_rate"),
                    record.get("host_acceptance_rate"),
                    getBoolean(record, "host_is_superhost"),
                    record.get("host_thumbnail_url"),
                    record.get("host_picture_url"),
                    record.get("host_neighbourhood"),
                    getInteger(record, "host_listings_count"),
                    getInteger(record, "host_total_listings_count"),
                    record.get("host_verifications"),
                    getBoolean(record, "host_has_profile_pic"),
                    getBoolean(record, "host_identity_verified"),
                    record.get("neighbourhood"),
                    getBoolean(record, "neighbourhood_cleansed"),
                    getBoolean(record, "neighbourhood_group_cleansed"),
                    getBigDecimal(record, "latitude", 5),
                    getBigDecimal(record, "longitude", 5),
                    record.get("property_type"),
                    record.get("room_type"),
                    getInteger(record, "accommodates"),
                    getDouble(record, "bathrooms"),
                    record.get("bathrooms_text"),
                    getInteger(record, "bedrooms"),
                    getInteger(record, "beds"),
                    record.get("price"),
                    getInteger(record, "minimum_nights"),
                    getInteger(record, "maximum_nights"),
                    getInteger(record, "minimum_minimum_nights"),
                    getInteger(record, "minimum_maximum_nights"),
                    getInteger(record, "maximum_minimum_nights"),
                    getInteger(record, "maximum_maximum_nights"),
                    getMaxFloat(record, "minimum_nights_avg_ntm", 2000F),
                    getMaxFloat(record, "maximum_nights_avg_ntm", 2000F),
                    record.get("calendar_updated"),
                    getBoolean(record, "has_availability"),
                    getInteger(record, "availability_30"),
                    getInteger(record, "availability_60"),
                    getInteger(record, "availability_90"),
                    getInteger(record, "availability_365"),
                    convertDate(record.get("calendar_last_scraped")),
                    getInteger(record, "number_of_reviews"),
                    getInteger(record, "number_of_reviews_ltm"),
                    getInteger(record, "number_of_reviews_l30d"),
                    convertDate(record.get("first_review")),
                    convertDate(record.get("last_review")),
                    getBigDecimal(record, "review_scores_rating", 2),
                    getBigDecimal(record, "review_scores_accuracy", 2),
                    getBigDecimal(record, "review_scores_cleanliness", 2),
                    getBigDecimal(record, "review_scores_checkin", 2),
                    getBigDecimal(record, "review_scores_communication", 2),
                    getBigDecimal(record, "review_scores_location", 2),
                    getBigDecimal(record, "review_scores_value", 2),
                    record.get("license"),
                    getBoolean(record, "instant_bookable"),
                    getInteger(record, "calculated_host_listings_count"),
                    getInteger(record, "calculated_host_listings_count_entire_homes"),
                    getInteger(record, "calculated_host_listings_count_private_rooms"),
                    getInteger(record, "calculated_host_listings_count_shared_rooms"),
                    getBigDecimal(record, "reviews_per_month", 2),
                    null
            );
            if (count % 500 == 0) {
                log.info(String.format("Saving listing %s.", count));
            }
            listingRepository.save(listing);
        }
        log.info(String.format("Wrote %s listings.", count));
    }

    private Integer getInteger(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    log.warn(String.format("Could not parse Integer %s", value));
                }
            }
        }
        return null;
    }

    private Long getLong(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    return Long.parseLong(value);
                } catch (NumberFormatException e) {
                    log.warn(String.format("Could not parse Float %s", value));
                }
            }
        }
        return null;
    }

    private boolean getBoolean(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    return Boolean.parseBoolean(value);
                } catch (NumberFormatException e) {
                    log.warn(String.format("Could not parse Boolean %s", value));
                }
            }
        }
        return false;
    }

    private Float getMaxFloat(CSVRecord record, String columnName, Float max) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    float rawValue = Float.parseFloat(value);
                    if (rawValue > max) {
                        return max;
                    }
                    return rawValue;
                } catch (NumberFormatException e) {
                    log.warn(String.format("Could not parse Float %s", value));
                }
            }
        }
        return null;
    }

    private Double getDouble(CSVRecord record, String columnName) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    return Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    log.warn(String.format("Could not parse Double %s", value));
                }
            }
        }
        return null;
    }

    private BigDecimal getBigDecimal(CSVRecord record, String columnName, int scale) {
        if (record != null && columnName != null) {
            String value = record.get(columnName);
            if (value != null && !value.isEmpty()) {
                try {
                    double d = Double.parseDouble(value);
                    return BigDecimal.valueOf(d).setScale(scale, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    log.warn(String.format("Could not parse BigDecimal %s", value));
                }
            }
        }
        return null;
    }

    public Date convertDate(String value) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        if (value != null && !value.isEmpty()) {
            try {
                return formatter.parse(value);
            } catch (ParseException e) {
                log.warn(String.format("Could not parse date %s", value));
            }
        }
        return null;
    }

}
