package com.med.banking.controller;

import com.med.banking.dto.AccountDTO;
import com.med.banking.dto.TransactionDTO;
import com.med.banking.dto.TransferFundDTO;
import com.med.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")

public class AccountController {


    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Add account Rest api
    @PostMapping("/create-account")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO) {
        return new ResponseEntity<>(accountService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    //Get Account Rest Api
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountbyId(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));

    }

    // Deposit Rest API
    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        AccountDTO accountDTO = accountService.deposit(id, amount);
        return ResponseEntity.ok(accountDTO);
    }

    // withdraw amount Rest API
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request) {
            Double amount = request.get("withdraw_amount");
            AccountDTO accountDTO = accountService.withdraw(id, amount);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    // Get All Accounts Rest API
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());


    }

    //Delete Account Rest API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount( @PathVariable Long id){
        accountService.deleteAccount(id);
        return  ResponseEntity.ok("Account deleted successfully");

    }

    //Build transfer REST API

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDTO transferFundDTO){
        accountService.transferFunds(transferFundDTO);
        return ResponseEntity.ok("Transfer processed successfully");
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(@PathVariable Long accountId){
        return ResponseEntity.ok(accountService.getAccountTransactions(accountId));
    }



}




