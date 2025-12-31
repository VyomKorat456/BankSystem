package com.example.account_service.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionRequestDTO {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
}
