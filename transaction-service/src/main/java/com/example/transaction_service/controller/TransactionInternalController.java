package com.example.transaction_service.controller;

import com.example.transaction_service.DTO.request.TransactionRequestDTO;
import com.example.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions/internal")
@RequiredArgsConstructor
public class TransactionInternalController {

    private final TransactionService service;

    @PostMapping
    public com.example.transaction_service.DTO.response.TransactionResponseDTO create(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return service.create(transactionRequestDTO);
    }
}
