package com.byteworksinc.airbnb.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "etl")
public record ETLPropertiesConfig(boolean clearAirbnbListingsTable, boolean airbnbLoadListings, boolean embedListings) {
}
