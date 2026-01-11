package com.myproject.pavzar.apigateway.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class InventoryServiceRoutes {

    @Value("${inventoryBaseUrl}")
    private String inventoryBaseUrl;

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes(){
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.GET("/api/v1/inventory/venue/{venueId}"),
                        request -> forwardWithPathVariable(request, "venueId", inventoryBaseUrl + "/venue/"))
                .route(RequestPredicates.GET("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariable(request, "eventId", inventoryBaseUrl + "/event/"))
                .route(RequestPredicates.GET("/api/v1/inventory/events"), HandlerFunctions.http(inventoryBaseUrl + "/events"))
                .route(RequestPredicates.PUT("/api/v1/inventory/event/{eventId}/capacity/{capacity}"),
                        request -> forwardWithTwoPathVariables(request, "eventId", "capacity", inventoryBaseUrl + "/event/"))
                .build();
    }

    private static ServerResponse forwardWithPathVariable(ServerRequest request, String pathVariable, String baseUrl) throws Exception{
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(baseUrl + value).handle(request);
    }

    private static ServerResponse forwardWithTwoPathVariables(
            ServerRequest request,
            String pathVariable1,
            String pathVariable2,
            String baseUrl
    ) throws Exception {
        String value1 = request.pathVariable(pathVariable1);
        String value2 = request.pathVariable(pathVariable2);
        return HandlerFunctions.http(baseUrl + value1 + "/capacity/" + value2).handle(request);
    }
}
