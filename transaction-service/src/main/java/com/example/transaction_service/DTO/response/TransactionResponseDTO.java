package com.example.transaction_service.DTO.response;

import com.example.transaction_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private UUID id;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}
