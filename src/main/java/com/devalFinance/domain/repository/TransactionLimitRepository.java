package com.devalFinance.domain.repository;

import com.devalFinance.domain.model.TransactionLimit;

import java.util.Optional;
import java.util.UUID;

public interface TransactionLimitRepository {
    TransactionLimit save(TransactionLimit transactionLimit);
    Optional<TransactionLimit> findById(UUID id);
    Optional<TransactionLimit> findByUserIdAndYearAndMonth(UUID userId, int year, int month);
}

