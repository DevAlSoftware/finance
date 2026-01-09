package com.devalFinance.application.usecase.account;

import com.devalFinance.application.dto.response.AccountResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetAccountsUseCase {
    
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public GetAccountsUseCase(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<AccountResponse> execute(UUID userId) {
        validateUserExists(userId);
        
        List<Account> accounts = accountRepository.findByUserIdAndActive(userId, true);
        
        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
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


