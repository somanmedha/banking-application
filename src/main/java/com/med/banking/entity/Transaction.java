package com.med.banking.entity;

import com.med.banking.constants.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Long accountId;
    private double amount;
    @Enumerated(value= EnumType.STRING)
    private TransactionType transactionType; // Deposit,withdraw,Transfer
    private LocalDateTime timestamp;
}
