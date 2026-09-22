package com.example.apiGateway.Filter;

import com.example.apiGateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        // Public endpoints
        if (path.equals("/auth/login") ||
                path.equals("/auth/register")) {

            return chain.filter(exchange);
        }

        // Get Authorization header
        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("Authorization");

        // No Authorization header
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return unauthorized(exchange);
        }

        // Extract JWT
        String token = authHeader.substring(7);

        // Validate JWT
        boolean valid = jwtService.validateToken(token);

        // Valid JWT
        if (valid) {
            return chain.filter(exchange);
        }

        // Invalid JWT
        return unauthorized(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {

        exchange.getResponse().setStatusCode(
                HttpStatus.UNAUTHORIZED
        );

        return exchange.getResponse().setComplete();
    }
}