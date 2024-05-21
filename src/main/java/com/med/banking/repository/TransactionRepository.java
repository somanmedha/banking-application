package com.med.banking.repository;

import com.med.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
 List<Transaction> findByAccountIdOrderByTimestampDesc( Long accountId);
}
