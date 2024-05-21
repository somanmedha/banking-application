package com.med.banking.service.impl;

import com.med.banking.constants.TransactionType;
import com.med.banking.dto.AccountDTO;
import com.med.banking.dto.TransactionDTO;
import com.med.banking.dto.TransferFundDTO;
import com.med.banking.entity.Account;
import com.med.banking.entity.Transaction;
import com.med.banking.exception.AccountException;
import com.med.banking.mapper.AccountMapper;
import com.med.banking.repository.AccountRepository;
import com.med.banking.repository.TransactionRepository;
import com.med.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public AccountDTO createAccount(AccountDTO accountDto) {
        Account account = AccountMapper.mapToAccountEntity(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        return AccountMapper.mapToAccountDto(account);

    }

    @Override
    public AccountDTO deposit(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        account.setBalance(account.getBalance() + amount);
        Account savedAccount = accountRepository.save(account);
        Transaction transaction= new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDTO withdraw(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));

        if (account.getBalance() < amount) {
            throw new AccountException("Low balance");
        }
        double revisedBalanceAfterWithdraw = account.getBalance() - amount;
        account.setBalance(revisedBalanceAfterWithdraw);
        Account savedAccount = accountRepository.save(account);
        Transaction transaction=new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDto)
                .sorted(Comparator.comparing(AccountDTO::id))
                .toList();


    }

    @Override
    public void deleteAccount(Long id) {

        accountRepository.findById(id).orElseThrow(() -> new AccountException("Account doesn't exist"));
        accountRepository.deleteById(id);


    }

    @Override
    public void transferFunds(TransferFundDTO transferFundDTO) {

        //Retrieving the account6 from which we send the amount
        Account fromAccount = accountRepository.findById(transferFundDTO.fromAccountId())
                .orElseThrow(() -> new AccountException("Account doesn't exist"));

        if(fromAccount.getBalance()< transferFundDTO.amount()){
            throw new RuntimeException("you have insufficient balance to make the transfer");
        }

        // Retrieving the account to which we need to send the amount
        Account toAccount = accountRepository.findById(transferFundDTO.toAccountId())
                .orElseThrow(() -> new AccountException("Account doesn't exist"));

        //Debiting the amount from fromAccount Object

        fromAccount.setBalance(fromAccount.getBalance() - transferFundDTO.amount());

        // Crediting the amount to toAccount Object

        toAccount.setBalance(toAccount.getBalance() + transferFundDTO.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundDTO.toAccountId());
        transaction.setAmount(transferFundDTO.amount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);


    }

    @Override
    public List<TransactionDTO> getAccountTransactions(Long accountId) {
        accountRepository.findById(accountId).orElseThrow(()->new AccountException("Account not found"));
         List<Transaction> transactions=transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        return  transactions.stream()
                 .map(this::mapToTransactionDTO)
                .sorted(Comparator.comparing(TransactionDTO::id))
                 .toList();

    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction){
        TransactionDTO transactionDTO=new TransactionDTO(
                transaction.getTransactionId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
        return transactionDTO;
    }
}
