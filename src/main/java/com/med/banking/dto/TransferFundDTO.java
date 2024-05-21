package com.med.banking.dto;

public record TransferFundDTO(Long fromAccountId, Long toAccountId, double amount) {
}

