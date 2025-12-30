package com.example.auth_service.controller;

import com.example.auth_service.DTO.request.LoginRequest;
import com.example.auth_service.DTO.request.RegisterRequest;
import com.example.auth_service.DTO.response.AuthResponse;
import com.example.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {

        log.info("🚀 AUTH CONTROLLER HIT → /auth/register");
        log.info("📩 RegisterRequest email={}", registerRequest.getEmail());

        authService.register(registerRequest);

        log.info("✅ User registration completed");

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        log.info("🚀 AUTH CONTROLLER HIT → /auth/login");
        log.info("📩 LoginRequest email={}", loginRequest.getEmail());

        AuthResponse response = authService.login(loginRequest);

        log.info("✅ Login successful, token generated");

        return ResponseEntity.ok(response);
    }
}
