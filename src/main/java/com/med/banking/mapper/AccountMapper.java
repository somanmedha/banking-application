package com.med.banking.mapper;

import com.med.banking.dto.AccountDTO;
import com.med.banking.entity.Account;

import java.util.Optional;

public class AccountMapper {

    public static Account mapToAccountEntity(AccountDTO accountDto){

        Account accountEntity =new Account(
                accountDto.id(),
                accountDto.accountHolderName(),
                accountDto.balance()
        );
        return accountEntity;
    }

    public static AccountDTO mapToAccountDto(Account account){

        AccountDTO accountDTO = new AccountDTO(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
        return accountDTO;
    }

}
