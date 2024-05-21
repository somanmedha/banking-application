package com.med.banking.dto;

import com.med.banking.constants.TransactionType;

import java.time.LocalDateTime;

public record TransactionDTO(Long id, Long accountId, double amount, TransactionType transactionType, LocalDateTime localDateTime) {
}
