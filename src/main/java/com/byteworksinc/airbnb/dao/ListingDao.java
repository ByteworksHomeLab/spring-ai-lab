package com.byteworksinc.airbnb.dao;

import com.byteworksinc.airbnb.entities.Listing;

import java.util.List;

public interface ListingDao {
    Listing save(Listing listing);

    List<Listing> findAll();

    Listing findById(Long id);

    void deleteListing(Long id);

    void deleteAll();
}
