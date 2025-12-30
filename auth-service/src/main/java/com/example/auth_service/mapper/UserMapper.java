package com.example.auth_service.mapper;

import com.example.auth_service.DTO.request.RegisterRequest;
import com.example.auth_service.entity.User;

public class UserMapper {
    public static User toEntity(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .role("ROLE_USER")
                .enabled(true)
                .build();
    }
}
