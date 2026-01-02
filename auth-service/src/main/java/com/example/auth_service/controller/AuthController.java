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

    @GetMapping("/validate")
    public ResponseEntity<com.example.auth_service.DTO.response.ValidateTokenResponse> validateToken(
            @RequestParam("token") String token) {
        log.info("🚀 AUTH CONTROLLER HIT → /auth/validate");
        return ResponseEntity.ok(authService.validateToken(token));
    }

    @PostMapping(value = "/token", consumes = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<java.util.Map<String, Object>> getToken(
            @RequestHeader(org.springframework.http.HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("grant_type") String grantType) {

        log.info("🔐 Auth Service Token Request. Grant Type: {}", grantType);
        log.info("🔑 Authorization Header: {}", authHeader);

        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "unsupported_grant_type"));
        }

        // Basic Auth Decoding (skipped for brevity, but ideally should validate
        // client_secret)
        // For this task, we assume the caller is trusted or we extract clientId from
        // Basic Auth
        String clientId = "unknown";
        String clientSecret = "";

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials),
                    java.nio.charset.StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            clientId = parts[0];
            clientSecret = parts.length > 1 ? parts[1] : "";
        }

        if (!authService.validateClient(clientId, clientSecret)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("error", "invalid_client"));
        }

        String token = authService.generateServiceToken(clientId, "transaction.write");

        return ResponseEntity.ok(java.util.Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "expires_in", 3600));
    }
}
