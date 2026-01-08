package com.devalFinance.application.usecase.transaction;

import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.model.Transaction;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DeleteTransactionUseCase {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public DeleteTransactionUseCase(TransactionRepository transactionRepository,
                                   AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void execute(UUID transactionId, UUID userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));
        
        if (!transaction.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar esta transacción");
        }
        
        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new IllegalStateException("Cuenta asociada no encontrada"));
        
        BigDecimal signedAmountToRevert = transaction.getSignedAmount().negate();
        account.updateBalance(signedAmountToRevert);
        accountRepository.save(account);
        
        transactionRepository.deleteById(transactionId);
    }
}

