package com.byteworksinc.airbnb.controllers;

import com.byteworksinc.airbnb.entities.Listing;
import com.byteworksinc.airbnb.repositories.ListingRepository;
import org.springframework.stereotype.Controller;
@Controller
public class ListingController {

    private final ListingRepository listingRepository;

    public ListingController(final ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public Listing insertListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public Listing findById(long id) {
        return listingRepository.findById(id);
    }
}
