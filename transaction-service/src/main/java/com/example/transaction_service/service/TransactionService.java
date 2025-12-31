package com.example.transaction_service.service;

import com.example.transaction_service.DTO.request.TransactionRequestDTO;
import com.example.transaction_service.DTO.response.TransactionResponseDTO;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.enums.TransactionStatus;
import com.example.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionResponseDTO create(TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .fromAccountId(transactionRequestDTO.getFromAccountId())
                .toAccountId(transactionRequestDTO.getToAccountId())
                .amount(transactionRequestDTO.getAmount())
                .status(TransactionStatus.SUCCESS)
                .build());

        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
