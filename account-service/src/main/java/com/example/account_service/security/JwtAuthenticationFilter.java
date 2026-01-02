package com.example.account_service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @org.springframework.beans.factory.annotation.Qualifier("authWebClient")
    private final org.springframework.web.reactive.function.client.WebClient authWebClient;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found in request headers");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            log.debug("Validating token with Auth Service...");

            com.example.account_service.DTO.response.ValidateTokenResponse validationResponse = authWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/auth/validate")
                            .queryParam("token", token)
                            .build())
                    .retrieve()
                    .bodyToMono(com.example.account_service.DTO.response.ValidateTokenResponse.class)
                    .block();

            if (validationResponse != null && validationResponse.isValid()) {
                String userId = validationResponse.getUserId();
                String role = validationResponse.getRole();

                log.info("Token valid. UserID: {}, Role: {}", userId, role);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(new SimpleGrantedAuthority(role)));

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Token validation failed or returned invalid");
                SecurityContextHolder.clearContext();
            }

        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
