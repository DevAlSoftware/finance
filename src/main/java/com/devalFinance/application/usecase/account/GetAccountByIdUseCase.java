package com.devalFinance.application.usecase.account;

import com.devalFinance.application.dto.response.AccountResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetAccountByIdUseCase {
    
    private final AccountRepository accountRepository;

    public GetAccountByIdUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse execute(UUID accountId, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        
        if (!account.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta cuenta");
        }
        
        if (!account.isActive()) {
            throw new IllegalStateException("La cuenta está inactiva");
        }
        
        return mapToResponse(account);
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUserId(),
                account.getName(),
                account.getAccountType().name(),
                account.getInitialBalance(),
                account.getCurrentBalance(),
                account.getCurrency(),
                account.getCreatedAt(),
                account.getActive()
        );
    }
}

