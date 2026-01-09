package com.devalFinance.application.usecase.account;

import com.devalFinance.application.dto.request.UpdateAccountRequest;
import com.devalFinance.application.dto.response.AccountResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class UpdateAccountUseCase {
    
    private final AccountRepository accountRepository;

    public UpdateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse execute(UUID accountId, UpdateAccountRequest request, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        
        if (!account.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para modificar esta cuenta");
        }
        
        if (!account.isActive()) {
            throw new IllegalStateException("No se puede modificar una cuenta inactiva");
        }
        
        updateAccountFields(account, request);
        Account updatedAccount = accountRepository.save(account);
        
        return mapToResponse(updatedAccount);
    }

    private void updateAccountFields(Account account, UpdateAccountRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            account.setName(request.getName());
        }
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


