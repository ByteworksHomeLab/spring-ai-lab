package com.byteworksinc.airbnb.repositories;

import com.byteworksinc.airbnb.entities.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    Listing findById(long id);
}
