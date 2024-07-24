package com.byteworksinc.airbnb.dao;

import com.byteworksinc.airbnb.entities.Listing;

import java.util.List;

public interface ListingDao {
    public Listing save(Listing listing);

    public List<Listing> findAll();

    public Listing findById(Long id);

    void deleteAll();
}
