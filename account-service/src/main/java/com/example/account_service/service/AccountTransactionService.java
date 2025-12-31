package com.example.account_service.service;

import com.example.account_service.DTO.request.TransactionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.account_service.DTO.response.TransactionResponseDTO;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

    private final WebClient transactionWebClient;

    public TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO) {
        return transactionWebClient.post()
                .uri("http://TRANSACTION-SERVICE/transactions/internal")
                .bodyValue(transactionRequestDTO)
                .retrieve()
                .bodyToMono(TransactionResponseDTO.class)
                .block();
    }
}
