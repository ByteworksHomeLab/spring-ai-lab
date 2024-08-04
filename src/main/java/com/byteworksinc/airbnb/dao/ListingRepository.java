package com.byteworksinc.airbnb.dao;

import com.byteworksinc.airbnb.entities.Listing;
import org.springframework.data.repository.ListCrudRepository;

public interface ListingRepository extends ListCrudRepository<Listing, Long> {

}
