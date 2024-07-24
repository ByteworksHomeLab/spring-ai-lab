drop table if exists listing;
create table listing
(
    id                                           bigint        not null
        constraint "PKListing_01"
            primary key,
    listing_url                                  text,
    scrape_id                                    bigint        not null,
    last_searched                                timestamp without time zone,
    last_scraped                                 timestamp without time zone,
    source                                       text,
    name                                         text,
    description                                  text,
    neighborhood_overview                        text,
    picture_url                                  text,
    host_id                                      bigint       not null,
    host_url                                     text          not null,
    host_name                                    text          not null,
    host_since                                   timestamp without time zone,
    host_location                                text,
    host_about                                   text,
    host_response_time                           varchar(18)   not null,
    host_response_rate                           varchar(4)    not null,
    host_acceptance_rate                         text          not null,
    host_is_superhost                            boolean       not null,
    host_thumbnail_url                           text          not null,
    host_picture_url                             text          not null,
    host_neighbourhood                           text,
    host_listings_count                          integer,
    host_total_listings_count                    integer,
    host_verifications                           text          not null,
    host_has_profile_pic                         boolean       not null,
    host_identity_verified                       boolean       not null,
    neighbourhood                                text          not null,
    latitude                                     numeric(7, 5) not null,
    longitude                                    numeric(7, 5) not null,
    property_type                                text,
    room_type                                    text,
    accommodates                                 integer,
    bathrooms                                    numeric(3, 1),
    bathrooms_text                               text,
    bedrooms                                     integer,
    beds                                         integer,
    price                                        varchar(10)   not null,
    minimum_nights                               integer       not null,
    maximum_nights                               integer       not null,
    minimum_minimum_nights                       integer,
    maximum_minimum_nights                       integer,
    minimum_maximum_nights                       integer,
    maximum_maximum_nights                       integer,
    minimum_nights_avg_ntm                       numeric(11, 2),
    maximum_nights_avg_ntm                       numeric(11, 2),
    calendar_updated                             text,
    has_availability                             boolean       not null,
    availability_30                              integer,
    availability_60                              integer,
    availability_90                              integer,
    availability_365                             integer,
    calendar_last_scraped                        timestamp,
    number_of_reviews                            integer       not null,
    number_of_reviews_ltm                        integer       not null,
    number_of_reviews_l30d                       integer,
    first_review                                 timestamp without time zone,
    last_review                                  timestamp without time zone,
    review_scores_rating                         integer,
    review_scores_accuracy                       integer,
    review_scores_cleanliness                    integer,
    review_scores_checkin                        integer,
    review_scores_communication                  integer,
    review_scores_location                       integer,
    review_scores_value                          integer,
    requires_license                             boolean,
    license                                      text,
    instant_bookable                             boolean,
    calculated_host_listings_count               integer       not null,
    calculated_host_listings_count_entire_homes  integer       not null,
    calculated_host_listings_count_private_rooms integer       not null,
    calculated_host_listings_count_shared_rooms  integer       not null,
    region_id                                    bigint,
    region_name                                  text,
    region_parent_id                             bigint,
    region_parent_name                           text,
    region_parent_parent_id                      bigint,
    region_parent_parent_name                    text,
    reviews_per_month                            numeric
);

create index "IDXAirbnb_02"
    on listing (host_id);

create index "IDXAirbnb_03"
    on listing (property_type);

create index "IDXAirbnb_04"
    on listing (room_type);

create index "IDXAirbnb_05"
    on listing (bedrooms);

