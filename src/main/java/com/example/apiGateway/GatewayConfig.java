package com.example.apiGateway;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("student-service", r -> r
                        .path("/students/**")
                        .uri("lb://student-service"))

                .route("course-service", r -> r
                        .path("/courses/**")
                        .uri("lb://course-service"))

                .build();
    }
}