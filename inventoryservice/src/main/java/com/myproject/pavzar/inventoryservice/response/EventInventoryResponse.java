package com.myproject.pavzar.inventoryservice.response;

import com.myproject.pavzar.inventoryservice.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInventoryResponse {
    private String event;
    private Long capacity;
    private Venue venue;
}
