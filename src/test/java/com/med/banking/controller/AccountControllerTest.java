package com.med.banking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.banking.constants.TransactionType;
import com.med.banking.dto.AccountDTO;
import com.med.banking.dto.TransactionDTO;
import com.med.banking.dto.TransferFundDTO;
import com.med.banking.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO(null, "John Doe", 1000.0);
        AccountDTO savedAccountDTO = new AccountDTO(1L, "John Doe", 1000.0);

        Mockito.when(accountService.createAccount(any(AccountDTO.class))).thenReturn(savedAccountDTO);

        mockMvc.perform(post("/api/accounts/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void testGetAccountById() throws Exception {
        AccountDTO accountDTO = new AccountDTO(1L, "John Doe", 1000.0);

        Mockito.when(accountService.getAccountById(1L)).thenReturn(accountDTO);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void testDeposit() throws Exception {
        AccountDTO accountDTO = new AccountDTO(1L, "John Doe", 1500.0);

        Mockito.when(accountService.deposit(eq(1L), any(Double.class))).thenReturn(accountDTO);

        mockMvc.perform(put("/api/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 500.0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1500.0));
    }

    @Test
    void testWithdraw() throws Exception {
        AccountDTO accountDTO = new AccountDTO(1L, "John Doe", 500.0);

        Mockito.when(accountService.withdraw(eq(1L), any(Double.class))).thenReturn(accountDTO);

        mockMvc.perform(put("/api/accounts/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("withdraw_amount", 500.0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(500.0));
    }

    @Test
    void testGetAllAccounts() throws Exception {
        List<AccountDTO> accounts = Arrays.asList(
                new AccountDTO(1L, "John Doe", 1000.0),
                new AccountDTO(2L, "Jane Doe", 2000.0)
        );

        Mockito.when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].accountHolderName").value("John Doe"))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].accountHolderName").value("Jane Doe"))
                .andExpect(jsonPath("$[1].balance").value(2000.0));
    }

    @Test
    void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/api/accounts/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully"));

        verify(accountService, times(1)).deleteAccount(1L);
    }

    @Test
    void testTransferFund() throws Exception {
        TransferFundDTO transferFundDTO = new TransferFundDTO(1L, 2L, 500.0);

        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferFundDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer processed successfully"));

        verify(accountService, times(1)).transferFunds(any(TransferFundDTO.class));
    }

    @Test
    void testGetAccountTransactions() throws Exception {
        List<TransactionDTO> transactions = Arrays.asList(
                new TransactionDTO(1L, 1L, 100.0, TransactionType.DEPOSIT, LocalDateTime.now()),
                new TransactionDTO(2L, 1L, 50.0, TransactionType.WITHDRAW, LocalDateTime.now())
        );

        Mockito.when(accountService.getAccountTransactions(1L)).thenReturn(transactions);

        mockMvc.perform(get("/api/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].transactionType").value(TransactionType.DEPOSIT.toString()))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].amount").value(50.0))
                .andExpect(jsonPath("$[1].transactionType").value(TransactionType.WITHDRAW.toString()));
    }
}
