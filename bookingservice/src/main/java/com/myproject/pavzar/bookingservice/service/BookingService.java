package com.myproject.pavzar.bookingservice.service;

import com.myproject.pavzar.bookingservice.client.InventoryServiceClient;
import com.myproject.pavzar.bookingservice.entity.Customer;
import com.myproject.pavzar.bookingservice.event.BookingEvent;
import com.myproject.pavzar.bookingservice.repository.CustomerRepository;
import com.myproject.pavzar.bookingservice.request.BookingRequest;
import com.myproject.pavzar.bookingservice.response.BookingResponse;
import com.myproject.pavzar.bookingservice.response.InventoryResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> bookingEventKafkaTemplate;

    public BookingService(CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient, KafkaTemplate<String, BookingEvent> bookingEventKafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.bookingEventKafkaTemplate = bookingEventKafkaTemplate;
    }

    public BookingResponse createBooking(BookingRequest request){
        // check if user exists
        Customer customer = customerRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("customer not found: " + request.getUserId()));

        // check if there is enough inventory
        InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        log.info("Inventory Response: {}", inventoryResponse);

        System.out.println(inventoryResponse);
        // check if there is enough inventory
        if(inventoryResponse.getCapacity() < request.getTicketCount()){
            throw new RuntimeException("Not enough inventory");
        }
        // create booking
        BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);

        // send booking to Order Service on a Kafka topic
        bookingEventKafkaTemplate.send("booking", bookingEvent);
        log.info("Booking sent to Kafka: {}", bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getEventId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }


    private BookingEvent createBookingEvent(BookingRequest request, Customer customer, InventoryResponse inventoryResponse){
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }

}

