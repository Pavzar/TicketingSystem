package com.myproject.pavzar.inventoryservice.controller;

import com.myproject.pavzar.inventoryservice.response.EventInventoryResponse;
import com.myproject.pavzar.inventoryservice.response.VenueInventoryResponse;
import com.myproject.pavzar.inventoryservice.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory/events")
    public List<EventInventoryResponse> inventoryGetAllEvents(){
        return inventoryService.getAllEvents();
    }

    @GetMapping("/inventory/venue/{venueId}")
    public VenueInventoryResponse inventoryByVenueId(@PathVariable Long venueId){
        return inventoryService.getVenueInformation(venueId);
    }
}
