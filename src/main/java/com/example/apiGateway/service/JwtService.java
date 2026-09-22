package com.example.apiGateway.service;

public interface JwtService {

    boolean validateToken(String token);

    String getRole(String token);
}