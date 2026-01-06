package com.myproject.pavzar.bookingservice.service;

import com.myproject.pavzar.bookingservice.client.InventoryServiceClient;
import com.myproject.pavzar.bookingservice.entity.Customer;
import com.myproject.pavzar.bookingservice.repository.CustomerRepository;
import com.myproject.pavzar.bookingservice.request.BookingRequest;
import com.myproject.pavzar.bookingservice.response.BookingResponse;
import com.myproject.pavzar.bookingservice.response.InventoryResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;


    public BookingService(CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public BookingResponse createBooking(BookingRequest request){
        // check if user exists
        Customer customer = customerRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("customer not found: " + request.getUserId()));

        // check if there is enough inventory
        InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        System.out.println(inventoryResponse);
        // check if there is enough inventory
        if(inventoryResponse.getCapacity() < request.getTicketCount()){
            throw new RuntimeException("Not enough inventory");
        }

        return BookingResponse.builder().build();
    }
}
