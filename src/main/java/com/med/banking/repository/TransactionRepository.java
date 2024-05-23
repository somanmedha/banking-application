package com.med.banking.repository;

import com.med.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
 List<Transaction> findByAccountIdOrderByTimestampDesc( Long accountId);
}
