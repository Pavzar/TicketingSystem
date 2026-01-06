package com.myproject.pavzar.inventoryservice.repository;

import com.myproject.pavzar.inventoryservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
}
