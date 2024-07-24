package com.byteworksinc.airbnb.controllers;

import com.byteworksinc.airbnb.dao.ListingDao;
import com.byteworksinc.airbnb.entities.Listing;
import org.springframework.stereotype.Controller;

@Controller
public class ListingController {

    private final ListingDao listingDao;

    public ListingController(final ListingDao listingDao) {
        this.listingDao = listingDao;
    }

    public Listing insertListing(Listing listing) {
        return listingDao.save(listing);
    }

    public Listing findById(long id) {
        return listingDao.findById(id);
    }
}
