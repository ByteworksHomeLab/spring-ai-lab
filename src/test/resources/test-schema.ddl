DROP TABLE IF EXISTS listing;
CREATE TABLE listing
(
    id                                           BIGINT        not null
        CONSTRAINT "PKListing_01"
            PRIMARY KEY,
    listing_url                                  TEXT,
    scrape_id                                    BIGINT        not null,
    last_searched                                TIMESTAMP without time zone,
    last_scraped                                 TIMESTAMP without time zone,
    source                                       TEXT,
    name                                         TEXT,
    description                                  TEXT,
    neighborhood_overview                        TEXT,
    picture_url                                  TEXT,
    host_id                                      BIGINT        not null,
    host_url                                     TEXT          not null,
    host_name                                    TEXT          not null,
    host_since                                   TIMESTAMP without time zone,
    host_location                                TEXT,
    host_about                                   TEXT,
    host_response_time                           VARCHAR(18)   not null,
    host_response_rate                           VARCHAR(4)    not null,
    host_acceptance_rate                         TEXT          not null,
    host_is_superhost                            BOOLEAN       not null,
    host_thumbnail_url                           TEXT          not null,
    host_picture_url                             TEXT          not null,
    host_neighbourhood                           TEXT,
    host_listings_count                          INTEGER,
    host_total_listings_count                    INTEGER,
    host_verifications                           TEXT          not null,
    host_has_profile_pic                         BOOLEAN       not null,
    host_identity_verified                       BOOLEAN       not null,
    neighbourhood                                TEXT          not null,
    latitude                                     NUMERIC(7, 5) not null,
    longitude                                    NUMERIC(7, 5) not null,
    property_type                                TEXT,
    room_type                                    TEXT,
    accommodates                                 INTEGER,
    bathrooms                                    NUMERIC(3, 1),
    bathrooms_text                               TEXT,
    bedrooms                                     INTEGER,
    beds                                         INTEGER,
    price                                        VARCHAR(10)   not null,
    minimum_nights                               INTEGER       not null,
    maximum_nights                               INTEGER       not null,
    minimum_minimum_nights                       INTEGER,
    maximum_minimum_nights                       INTEGER,
    minimum_maximum_nights                       INTEGER,
    maximum_maximum_nights                       INTEGER,
    minimum_nights_avg_ntm                       NUMERIC(11, 2),
    maximum_nights_avg_ntm                       NUMERIC(11, 2),
    calendar_updated                             TEXT,
    has_availability                             BOOLEAN       not null,
    availability_30                              INTEGER,
    availability_60                              INTEGER,
    availability_90                              INTEGER,
    availability_365                             INTEGER,
    calendar_last_scraped                        TIMESTAMP,
    number_of_reviews                            INTEGER       not null,
    number_of_reviews_ltm                        INTEGER       not null,
    number_of_reviews_l30d                       INTEGER,
    first_review                                 TIMESTAMP without time zone,
    last_review                                  TIMESTAMP without time zone,
    review_scores_rating                         INTEGER,
    review_scores_accuracy                       INTEGER,
    review_scores_cleanliness                    INTEGER,
    review_scores_checkin                        INTEGER,
    review_scores_communication                  INTEGER,
    review_scores_location                       INTEGER,
    review_scores_value                          INTEGER,
    requires_license                             BOOLEAN,
    license                                      TEXT,
    instant_bookable                             BOOLEAN,
    calculated_host_listings_count               INTEGER       not null,
    calculated_host_listings_count_entire_homes  INTEGER       not null,
    calculated_host_listings_count_private_rooms INTEGER       not null,
    calculated_host_listings_count_shared_rooms  INTEGER       not null,
    region_id                                    BIGINT,
    region_name                                  TEXT,
    region_parent_id                             BIGINT,
    region_parent_name                           TEXT,
    region_parent_parent_id                      BIGINT,
    region_parent_parent_name                    TEXT,
    reviews_per_month                            NUMERIC
);

CREATE INDEX "IDXAirbnb_02"
    on listing (host_id);

CREATE INDEX "IDXAirbnb_03"
    on listing (property_type);

CREATE INDEX "IDXAirbnb_04"
    on listing (room_type);

CREATE INDEX "IDXAirbnb_05"
    on listing (bedrooms);

