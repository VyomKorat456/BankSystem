package com.example.auth_service.service;

import com.example.auth_service.DTO.request.LoginRequest;
import com.example.auth_service.DTO.request.RegisterRequest;
import com.example.auth_service.DTO.response.AuthResponse;
import com.example.auth_service.DTO.response.ValidateTokenResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    ValidateTokenResponse validateToken(String token);

    String generateServiceToken(String clientId, String scope);

    boolean validateClient(String clientId, String clientSecret);
}
