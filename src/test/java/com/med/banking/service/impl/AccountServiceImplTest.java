package com.med.banking.service.impl;

import com.med.banking.constants.TransactionType;
import com.med.banking.dto.AccountDTO;
import com.med.banking.dto.TransactionDTO;
import com.med.banking.dto.TransferFundDTO;
import com.med.banking.entity.Account;
import com.med.banking.entity.Transaction;
import com.med.banking.exception.AccountException;
import com.med.banking.repository.AccountRepository;
import com.med.banking.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;


    private MockitoSession mockitoSession;

    @BeforeEach
    void setUp() {
       mockitoSession = Mockito.mockitoSession().initMocks(this).startMocking();
    }

    @AfterEach
    void tearDown() {
        mockitoSession.finishMocking();
    }

    // 1.  Test Case for the method: AccountDTO createAccount(AccountDTO accountDto)
    @Test
    void testCreateAccount() {
        // Arrange
        AccountDTO accountDTO = new AccountDTO(null, "John Doe", 1000.0);
        Account accountEntity = new Account(null, "John Doe", 1000.0);
        Account savedAccount = new Account(1L, "John Doe", 1000.0);

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        AccountDTO result = accountService.createAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.accountHolderName());
        assertEquals(1000.0, result.balance(), 0.0);
        assertEquals(1L, result.id());
    }

    // 2.1 Test case for method: AccountDTO getAccountById(Long id)
    @Test
    void testGetAccountById() {
        // Arrange
        Long accountId = 1L;
        Account account = new Account(accountId, "John Doe", 1000.0);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        AccountDTO result = accountService.getAccountById(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(accountId, result.id());
        assertEquals("John Doe", result.accountHolderName());
        assertEquals(1000.0, result.balance(), 0.0);

        // Verify that the repository findById method was called once
        verify(accountRepository, times(1)).findById(accountId);
    }

    // 2.2 Test case for Exception handling for method: AccountDTO getAccountById(Long id)
    @Test
    void testGetAccountById_AccountNotFound() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.getAccountById(accountId);
        });

        assertEquals("Account doesn't exist", exception.getMessage());

        // Verify that the repository findById method was called once
        verify(accountRepository, times(1)).findById(accountId);
    }

    // 3.1 Test case for method: AccountDTO deposit(Long id, double amount)
    @Test
    void testDeposit() {
        // Arrange
        Long accountId = 1L;
        double depositAmount = 500.0;
        Account account = new Account(accountId, "John Doe", 1000.0);
        Account updatedAccount = new Account(accountId, "John Doe", 1500.0);
        Transaction transaction = new Transaction(1L, accountId, depositAmount, TransactionType.DEPOSIT, LocalDateTime.now());

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        AccountDTO result = accountService.deposit(accountId, depositAmount);

        // Assert
        assertNotNull(result);
        assertEquals(accountId, result.id());
        assertEquals(1500.0, result.balance(), 0.0);

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // 3.2 Test case for Exception handling for  method: AccountDTO deposit(Long id, double amount)
    @Test
    void testDeposit_AccountNotFound() {
        // Arrange
        Long accountId = 1L;
        double depositAmount = 500.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.deposit(accountId, depositAmount);
        });

        assertEquals("Account doesn't exist", exception.getMessage());

        // Verify that the repository findById method was called once
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    // 4.1 Test case for method: AccountDTO withdraw(Long id, double amount)
    @Test
    void testWithdraw() {
        // Arrange
        Long accountId = 1L;
        double withdrawAmount = 500.0;
        Account account = new Account(accountId, "John Doe", 1000.0);
        Account updatedAccount = new Account(accountId, "John Doe", 500.0);
        Transaction transaction = new Transaction(1L, accountId, withdrawAmount, TransactionType.WITHDRAW, LocalDateTime.now());

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        AccountDTO result = accountService.withdraw(accountId, withdrawAmount);

        // Assert
        assertNotNull(result);
        assertEquals(accountId, result.id());
        assertEquals(500.0, result.balance(), 0.0);

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // 4.2 Test case for exception handling (Account not found)for method: AccountDTO withdraw(Long id, double amount)
    @Test
    void testWithdraw_AccountNotFound() {
        // Arrange
        Long accountId = 1L;
        double withdrawAmount = 500.0;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.withdraw(accountId, withdrawAmount);
        });

        assertEquals("Account doesn't exist", exception.getMessage());

        // Verify that the repository findById method was called once
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    // 4.3 Test case for exception handling (Low Balance)for method: AccountDTO withdraw(Long id, double amount)
    @Test
    void testWithdraw_LowBalance() {
        // Arrange
        Long accountId = 1L;
        double withdrawAmount = 1500.0;
        Account account = new Account(accountId, "John Doe", 1000.0);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.withdraw(accountId, withdrawAmount);
        });

        assertEquals("Low balance", exception.getMessage());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    // 5.1 Test case for method:List<AccountDTO> getAllAccounts()
    @Test
    void testGetAllAccounts() {
        // Arrange
        Account account1 = new Account(1L, "John Doe", 1000.0);
        Account account2 = new Account(2L, "Jane Doe", 2000.0);
        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<AccountDTO> result = accountService.getAllAccounts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("John Doe", result.get(0).accountHolderName());
        assertEquals(1000.0, result.get(0).balance(), 0.0);
        assertEquals(2L, result.get(1).id());
        assertEquals("Jane Doe", result.get(1).accountHolderName());
        assertEquals(2000.0, result.get(1).balance(), 0.0);

        // Verify that the repository findAll method was called once
        verify(accountRepository, times(1)).findAll();
    }

    // 5.2 Test case when no accounts are found for method :List<AccountDTO> getAllAccounts()
    @Test
    void testGetAllAccounts_NoAccounts() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<AccountDTO> result = accountService.getAllAccounts();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        // Verify that the repository findAll method was called once
        verify(accountRepository, times(1)).findAll();
    }

    //6.1 Test case for method:void deleteAccount(Long id)
    @Test
    void testDeleteAccount() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(new Account(accountId, "John Doe", 1000.0)));

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).deleteById(accountId);
    }

    //6.2  Test case for exception handling for method:void deleteAccount(Long id)
    @Test
    void testDeleteAccount_AccountNotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.deleteAccount(accountId);
        });

        assertEquals("Account doesn't exist", exception.getMessage());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(0)).deleteById(accountId);
    }

    // 7.1  Test case for method: public void transferFunds(TransferFundDTO transferFundDTO)
    // Successful transfer of funds.
    @Test
    void testTransferFunds() {
        // Arrange
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        double transferAmount = 500.0;

        Account fromAccount = new Account(fromAccountId, "John Doe", 1000.0);
        Account toAccount = new Account(toAccountId, "Jane Doe", 2000.0);
        TransferFundDTO transferFundDTO = new TransferFundDTO(fromAccountId, toAccountId, transferAmount);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        // Act
        accountService.transferFunds(transferFundDTO);

        // Assert
        assertEquals(500.0, fromAccount.getBalance(), 0.0);
        assertEquals(2500.0, toAccount.getBalance(), 0.0);

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(fromAccountId);
        verify(accountRepository, times(1)).findById(toAccountId);
        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // 7.2  Test case for exception handling for method: public void transferFunds(TransferFundDTO transferFundDTO)
    //Transfer when the source account doesn't exist.
    @Test
    void testTransferFunds_FromAccountNotFound() {
        // Arrange
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        double transferAmount = 500.0;
        TransferFundDTO transferFundDTO = new TransferFundDTO(fromAccountId, toAccountId, transferAmount);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.transferFunds(transferFundDTO);
        });

        assertEquals("Account doesn't exist", exception.getMessage());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(fromAccountId);
        verify(accountRepository, times(0)).findById(toAccountId);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    // 7.3 Test case for exception handling for method: public void transferFunds(TransferFundDTO transferFundDTO)
    //Transfer when the destination account doesn't exist.
    @Test
    void testTransferFunds_ToAccountNotFound() {
        // Arrange
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        double transferAmount = 500.0;
        Account fromAccount = new Account(fromAccountId, "John Doe", 1000.0);
        TransferFundDTO transferFundDTO = new TransferFundDTO(fromAccountId, toAccountId, transferAmount);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.transferFunds(transferFundDTO);
        });

        assertEquals("Account doesn't exist", exception.getMessage());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(fromAccountId);
        verify(accountRepository, times(1)).findById(toAccountId);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    //7.4  Test case for exception handling for method: public void transferFunds(TransferFundDTO transferFundDTO)
    //Transfer when the source account has insufficient balance.
    @Test
    void testTransferFunds_InsufficientBalance() {
        // Arrange
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        double transferAmount = 1500.0;
        Account fromAccount = new Account(fromAccountId, "John Doe", 1000.0);
        Account toAccount = new Account(toAccountId, "Jane Doe", 2000.0);
        TransferFundDTO transferFundDTO = new TransferFundDTO(fromAccountId, toAccountId, transferAmount);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.transferFunds(transferFundDTO);
        });

        assertEquals("you have insufficient balance to make the transfer", exception.getMessage());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(fromAccountId);
        verify(accountRepository, never()).findById(toAccountId);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    // 8.1 Test case for method: public List<TransactionDTO> getAccountTransactions(Long accountId)
    @Test
    void testGetAccountTransactions() {
        // Arrange
        Long accountId = 1L;
        Account account = new Account(accountId, "John Doe", 1000.0);
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, accountId, 100.0, TransactionType.DEPOSIT, LocalDateTime.now()),
                new Transaction(2L, accountId, 200.0, TransactionType.WITHDRAW, LocalDateTime.now())
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountIdOrderByTimestampDesc(accountId)).thenReturn(transactions);

        // Act
        List<TransactionDTO> result = accountService.getAccountTransactions(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(100.0, result.get(0).amount(), 0.0);
        assertEquals(TransactionType.DEPOSIT, result.get(0).transactionType());
        assertEquals(2L, result.get(1).id());
        assertEquals(200.0, result.get(1).amount(), 0.0);
        assertEquals(TransactionType.WITHDRAW, result.get(1).transactionType());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(transactionRepository, times(1)).findByAccountIdOrderByTimestampDesc(accountId);
    }

    //8.2   Test case for exception handling for method: public List<TransactionDTO> getAccountTransactions(Long accountId)
    @Test
    void testGetAccountTransactions_AccountNotFound() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class, () -> {
            accountService.getAccountTransactions(accountId);
        });

        assertEquals("Account not found", exception.getMessage());

        // Verify that the repository methods were called
        verify(accountRepository, times(1)).findById(accountId);
        verify(transactionRepository, times(0)).findByAccountIdOrderByTimestampDesc(accountId);
    }


}

