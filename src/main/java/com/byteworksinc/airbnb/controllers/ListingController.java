package com.byteworksinc.airbnb.controllers;

import com.byteworksinc.airbnb.dao.ListingRepository;
import com.byteworksinc.airbnb.entities.Listing;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ListingController {

    private final ListingRepository listingRepository;

    public ListingController(final ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public Listing insertListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public Optional<Listing> findById(long listingID) {
        return listingRepository.findById(listingID);
    }

    public void deleteListing(long listingID) {
        listingRepository.deleteById(listingID);
    }
}
