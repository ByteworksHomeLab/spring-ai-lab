CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS public.listing
(
    id                                           BIGINT        NOT NULL,
    listing_url                                  TEXT,
    scrape_id                                    BIGINT        NOT NULL,
    last_scraped                                 TIMESTAMP WITHOUT TIME ZONE,
    source                                       TEXT,
    name                                         TEXT,
    description                                  TEXT,
    description_embedding                        VECTOR(1536),
    neighborhood_overview                        TEXT,
    picture_url                                  TEXT,
    host_id                                      BIGINT        NOT NULL,
    host_url                                     TEXT          NOT NULL,
    host_name                                    TEXT          NOT NULL,
    host_since                                   TIMESTAMP WITHOUT TIME ZONE,
    host_location                                TEXT,
    host_about                                   TEXT,
    host_response_time                           varchar(18)   NOT NULL,
    host_response_rate                           varchar(4)    NOT NULL,
    host_acceptance_rate                         TEXT          NOT NULL,
    host_is_superhost                            BOOLEAN       NOT NULL,
    host_thumbnail_url                           TEXT          NOT NULL,
    host_picture_url                             TEXT          NOT NULL,
    host_neighbourhood                           TEXT,
    host_listings_count                          INTEGER,
    host_total_listings_count                    INTEGER,
    host_verifications                           TEXT          NOT NULL,
    host_has_profile_pic                         BOOLEAN       NOT NULL,
    host_identity_verified                       BOOLEAN       NOT NULL,
    neighbourhood                                TEXT          NOT NULL,
    neighbourhood_cleansed                       BOOLEAN,
    neighbourhood_group_cleansed                 BOOLEAN,
    latitude                                     NUMERIC(7, 5) NOT NULL,
    longitude                                    NUMERIC(7, 5) NOT NULL,
    property_type                                TEXT,
    room_type                                    TEXT,
    accommodates                                 INTEGER,
    bathrooms                                    NUMERIC(3, 1),
    bathrooms_text                               TEXT,
    bedrooms                                     INTEGER,
    beds                                         INTEGER,
    price                                        varchar(10)   NOT NULL,
    minimum_nights                               INTEGER       NOT NULL,
    maximum_nights                               INTEGER       NOT NULL,
    minimum_minimum_nights                       INTEGER,
    maximum_minimum_nights                       INTEGER,
    minimum_maximum_nights                       INTEGER,
    maximum_maximum_nights                       INTEGER,
    minimum_nights_avg_ntm                       NUMERIC(7, 2),
    maximum_nights_avg_ntm                       NUMERIC(7, 2),
    calendar_updated                             TEXT,
    has_availability                             BOOLEAN       NOT NULL,
    availability30                               INTEGER,
    availability60                               INTEGER,
    availability90                               INTEGER,
    availability365                              INTEGER,
    calendar_last_scraped                        TIMESTAMP WITHOUT TIME ZONE,
    number_of_reviews                            INTEGER       NOT NULL,
    number_of_reviews_ltm                        INTEGER       NOT NULL,
    number_of_reviews_l30d                       INTEGER,
    first_review                                 TIMESTAMP WITHOUT TIME ZONE,
    last_review                                  TIMESTAMP WITHOUT TIME ZONE,
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
    calculated_host_listings_count               INTEGER       NOT NULL,
    calculated_host_listings_count_entire_homes  INTEGER       NOT NULL,
    calculated_host_listings_count_private_rooms INTEGER       NOT NULL,
    calculated_host_listings_count_shared_rooms  INTEGER       NOT NULL,
    region_id                                    BIGINT,
    region_name                                  TEXT,
    region_parent_id                             BIGINT,
    region_parent_name                           TEXT,
    region_parent_parent_id                      BIGINT,
    region_parent_parent_name                    TEXT,
    reviews_per_month                            NUMERIC(5, 2),
    version                                      INTEGER,
    CONSTRAINT "PKListing_01" PRIMARY KEY ("id")
);

CREATE INDEX IF NOT EXISTS "IDXAirbnb_02"
    on public.listing (host_id);

CREATE INDEX IF NOT EXISTS "IDXAirbnb_03"
    on public.listing (property_type);

CREATE INDEX IF NOT EXISTS "IDXAirbnb_04"
    on public.listing (room_type);

CREATE INDEX IF NOT EXISTS "IDXAirbnb_05"
    on public.listing (bedrooms);
