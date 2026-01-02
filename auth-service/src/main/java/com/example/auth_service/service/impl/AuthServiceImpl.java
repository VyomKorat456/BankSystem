package com.example.auth_service.service.impl;

import com.example.auth_service.DTO.request.LoginRequest;
import com.example.auth_service.DTO.request.RegisterRequest;
import com.example.auth_service.DTO.response.AuthResponse;
import com.example.auth_service.DTO.response.ValidateTokenResponse;
import com.example.auth_service.entity.User;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.JwtUtil;
import com.example.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = UserMapper.toEntity(
                registerRequest,
                passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User disabled");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token, "Bearer");
    }

    @Override
    public com.example.auth_service.DTO.response.ValidateTokenResponse validateToken(String token) {
        try {
            var claims = jwtUtil.validateToken(token);
            return com.example.auth_service.DTO.response.ValidateTokenResponse.builder()
                    .valid(true)
                    .userId(claims.getSubject())
                    .role(claims.get("role", String.class))
                    .build();
        } catch (Exception e) {
            return com.example.auth_service.DTO.response.ValidateTokenResponse.builder()
                    .valid(false)
                    .build();
        }
    }

    @org.springframework.beans.factory.annotation.Value("${spring.security.clients.account-service.secret}")
    private String accountServiceSecret;

    @org.springframework.beans.factory.annotation.Value("${spring.security.clients.transaction-service.secret}")
    private String transactionServiceSecret;

    @Override
    public String generateServiceToken(String clientId, String scope) {
        // In a real scenario, we might validate clientId against a database
        return jwtUtil.generateServiceToken(clientId, "ROLE_SERVICE", scope);
    }

    @Override
    public boolean validateClient(String clientId, String clientSecret) {
        if ("account-service".equals(clientId)) {
            return accountServiceSecret.equals(clientSecret);
        } else if ("transaction-service".equals(clientId)) {
            return transactionServiceSecret.equals(clientSecret);
        }
        return false;
    }
}
