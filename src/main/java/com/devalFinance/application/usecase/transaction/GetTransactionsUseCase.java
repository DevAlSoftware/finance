package com.devalFinance.application.usecase.transaction;

import com.devalFinance.application.dto.response.TransactionResponse;
import com.devalFinance.domain.model.Transaction;
import com.devalFinance.domain.repository.TransactionRepository;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetTransactionsUseCase {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public GetTransactionsUseCase(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> execute(UUID userId, LocalDate startDate, LocalDate endDate) {
        validateUserExists(userId);
        
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        List<Transaction> transactions = transactionRepository
                .findByUserIdAndTransactionDateBetween(userId, startDate, endDate);
        
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
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

