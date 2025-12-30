package com.example.auth_service.service;

import com.example.auth_service.DTO.request.LoginRequest;
import com.example.auth_service.DTO.request.RegisterRequest;
import com.example.auth_service.DTO.response.AuthResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
