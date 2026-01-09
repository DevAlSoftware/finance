package com.devalFinance.application.usecase.account;

import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class DeleteAccountUseCase {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DeleteAccountUseCase(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void execute(UUID accountId, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        
        if (!account.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar esta cuenta");
        }
        
        validateAccountCanBeDeleted(accountId);
        
        account.setActive(false);
        accountRepository.save(account);
    }

    private void validateAccountCanBeDeleted(UUID accountId) {
        long transactionCount = transactionRepository.findByAccountId(accountId).size();
        if (transactionCount > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar una cuenta que tiene transacciones asociadas. " +
                    "En su lugar, la cuenta será desactivada."
            );
        }
    }
}


