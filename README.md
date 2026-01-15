# Ticketing Platform – Spring Boot Microservices

A compact **event ticketing** backend built with **Spring Boot 3 / Java 21** that demonstrates a practical microservices workflow using both **synchronous REST** calls and **asynchronous Kafka** messaging.

## Highlights

- **API Gateway** (Spring Cloud Gateway)
  - Central entrypoint + route definitions
  - JWT Resource Server security (Keycloak)
  - Resilience4j circuit breaker + Actuator health
  - Swagger UI
- **Inventory Service**
  - Stores venues & events in MySQL (Flyway migrations)
  - Exposes read endpoints for venue/event inventory
  - Updates remaining capacity when tickets are sold
- **Booking Service**
  - Validates available capacity by calling Inventory
  - Publishes a `BookingEvent` to Kafka (`booking` topic)
- **Order Service**
  - Consumes `BookingEvent` from Kafka
  - Persists the order and calls Inventory to decrement capacity

## Diagram

<img width="1082" height="667" alt="image" src="https://github.com/user-attachments/assets/99d11b6d-e854-4ec8-9abe-5a4adfad81a1" />

## Services

| Service | Purpose | Default port |
|---|---|---:|
| `inventoryservice` | Venues/events + remaining capacity (MySQL + Flyway) | `8080` |
| `bookingservice` | Booking API + Kafka producer | `8081` |
| `orderservice` | Kafka consumer + order persistence + inventory update | `8082` |
| `apigateway` | Routing + docs aggregation + security + resilience | `8090` |
| Kafka UI (infra) | Browse topics/messages | `8084` |

## Tech stack

- Java 21
- Spring Boot 3.4.x
- Spring Cloud 2024.0.0 (Gateway)
- Spring Data JPA (Hibernate) + MySQL 8
- Flyway DB migrations (Inventory service)
- Kafka (Confluent images) + Kafka UI
- Resilience4j (circuit breaker)
- SpringDoc OpenAPI / Swagger UI
- Keycloak for JWT auth at the gateway


## Security (Keycloak / JWT)

The gateway is configured as an **OAuth2 Resource Server** and (by default) protects all routes except docs.

## Project structure

```
├── apigateway/          # Gateway routes + security + aggregated Swagger UI
├── inventoryservice/    # Inventory REST API + Flyway migrations + docker-compose infra
├── bookingservice/      # Booking REST API + Kafka producer
└── orderservice/        # Kafka consumer + persistence + inventory update call
```

## API docs (Swagger)

- Aggregated Swagger UI (gateway): **http://localhost:8090/swagger-ui.html**
- Inventory Swagger UI: **http://localhost:8080/swagger-ui.html**
- Booking Swagger UI: **http://localhost:8081/swagger-ui.html**

The gateway exposes each service’s OpenAPI docs under:
- `/docs/inventoryservice/v3/api-docs`
- `/docs/bookingservice/v3/api-docs`



