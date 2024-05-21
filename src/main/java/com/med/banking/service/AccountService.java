package com.med.banking.service;

import com.med.banking.dto.AccountDTO;
import com.med.banking.dto.TransactionDTO;
import com.med.banking.dto.TransferFundDTO;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountDTO account);
    AccountDTO getAccountById(Long id);
    AccountDTO deposit(Long id, double amount);
    AccountDTO withdraw(Long id, double amount);
    List<AccountDTO> getAllAccounts();
    void deleteAccount(Long id);
    void transferFunds(TransferFundDTO transferFundDTO);
    List<TransactionDTO> getAccountTransactions(Long accountId);
}
