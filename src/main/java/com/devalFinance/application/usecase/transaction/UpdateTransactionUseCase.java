package com.devalFinance.application.usecase.transaction;

import com.devalFinance.application.dto.request.UpdateTransactionRequest;
import com.devalFinance.application.dto.response.TransactionResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.model.Transaction;
import com.devalFinance.domain.model.TransactionType;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class UpdateTransactionUseCase {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public UpdateTransactionUseCase(TransactionRepository transactionRepository,
                                   AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponse execute(UUID transactionId, UpdateTransactionRequest request, UUID userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));
        
        if (!transaction.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para modificar esta transacción");
        }
        
        Account originalAccount = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new IllegalStateException("Cuenta original no encontrada"));
        
        BigDecimal originalSignedAmount = transaction.getSignedAmount();
        
        TransactionType newType = request.getTransactionType() != null ?
                TransactionType.valueOf(request.getTransactionType().toUpperCase()) : transaction.getTransactionType();
        
        BigDecimal newAmount = request.getAmount() != null ? request.getAmount() : transaction.getAmount();
        
        Account newAccount = originalAccount;
        if (request.getAccountId() != null && !request.getAccountId().equals(transaction.getAccountId())) {
            newAccount = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Nueva cuenta no encontrada"));
            
            if (!newAccount.getUserId().equals(userId)) {
                throw new IllegalArgumentException("No tiene permisos para usar la cuenta especificada");
            }
            
            if (!newAccount.isActive()) {
                throw new IllegalStateException("La cuenta especificada está inactiva");
            }
        }
        
        BigDecimal newSignedAmount = newType == TransactionType.EXPENSE ? 
                newAmount.negate() : newAmount;
        
        if (!originalAccount.getId().equals(newAccount.getId())) {
            if (newType == TransactionType.EXPENSE && newAccount.getCurrentBalance().compareTo(newAmount) < 0) {
                throw new IllegalStateException("La cuenta destino no tiene saldo suficiente para esta transacción");
            }
            
            originalAccount.updateBalance(originalSignedAmount.negate());
            accountRepository.save(originalAccount);
            
            newAccount.updateBalance(newSignedAmount);
            accountRepository.save(newAccount);
        } else {
            BigDecimal balanceAfterRevert = newAccount.getCurrentBalance().subtract(originalSignedAmount);
            if (newType == TransactionType.EXPENSE && balanceAfterRevert.compareTo(newAmount) < 0) {
                throw new IllegalStateException("La cuenta no tiene saldo suficiente para esta transacción después de revertir la original");
            }
            
            BigDecimal balanceAdjustment = newSignedAmount.subtract(originalSignedAmount);
            newAccount.updateBalance(balanceAdjustment);
            accountRepository.save(newAccount);
        }
        
        updateTransactionFields(transaction, request, newAccount.getId(), newType);
        transaction.setUpdatedAt(java.time.LocalDateTime.now());
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        return mapToResponse(updatedTransaction);
    }

    private void updateTransactionFields(Transaction transaction, UpdateTransactionRequest request, 
                                        UUID accountId, TransactionType transactionType) {
        if (request.getAccountId() != null) {
            transaction.setAccountId(accountId);
        }
        
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        
        if (request.getTransactionType() != null) {
            transaction.setTransactionType(transactionType);
        }
        
        if (request.getCategoryId() != null) {
            transaction.setCategoryId(request.getCategoryId());
        }
        
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        
        if (request.getTransactionDate() != null) {
            transaction.setTransactionDate(request.getTransactionDate());
        }
        
        if (request.getTags() != null) {
            transaction.setTags(request.getTags());
        }
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getUserId(),
                transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getCategoryId(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getTags(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}

