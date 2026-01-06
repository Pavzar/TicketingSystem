package com.myproject.pavzar.inventoryservice.service;

import com.myproject.pavzar.inventoryservice.entity.Event;
import com.myproject.pavzar.inventoryservice.entity.Venue;
import com.myproject.pavzar.inventoryservice.repository.EventRepository;
import com.myproject.pavzar.inventoryservice.repository.VenueRepository;
import com.myproject.pavzar.inventoryservice.response.EventInventoryResponse;
import com.myproject.pavzar.inventoryservice.response.VenueInventoryResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public InventoryService(final EventRepository eventRepository, final VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    public List<EventInventoryResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream().map(event -> EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .build()).collect(Collectors.toList());
    }

    public VenueInventoryResponse getVenueInformation(Long venueId){
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new EntityNotFoundException("Venue not found: " + venueId));

        return VenueInventoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

    public EventInventoryResponse getEventInventory(Long eventId){
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        return EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .ticketPrice(event.getTicketPrice())
                .eventId(event.getId())
                .build();
    }

}
