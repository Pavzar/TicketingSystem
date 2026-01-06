package com.myproject.pavzar.inventoryservice.repository;

import com.myproject.pavzar.inventoryservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
