package com.devalFinance.domain.repository;

import com.devalFinance.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByUserId(UUID userId);
    List<Transaction> findByAccountId(UUID accountId);
    List<Transaction> findByUserIdAndTransactionDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByUserIdAndYear(UUID userId, int year);
    long countByUserIdAndYearAndMonth(UUID userId, int year, int month);
    void deleteById(UUID id);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}


