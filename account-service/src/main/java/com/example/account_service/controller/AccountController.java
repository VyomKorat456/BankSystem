package com.example.account_service.controller;

import com.example.account_service.DTO.request.TransactionRequestDTO;
import com.example.account_service.DTO.response.TransactionResponseDTO;
import com.example.account_service.service.AccountTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountTransactionService accountTransactionService;

    @PostMapping("/transfer")
    public TransactionResponseDTO transferMoney(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return accountTransactionService.transfer(transactionRequestDTO);
    }
}
