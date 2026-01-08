package com.devalFinance.infrastructure.persistence.adapter;

import com.devalFinance.domain.model.TransactionLimit;
import com.devalFinance.domain.repository.TransactionLimitRepository;
import com.devalFinance.infrastructure.persistence.entity.TransactionLimitEntity;
import com.devalFinance.infrastructure.persistence.repository.JpaTransactionLimitRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionLimitRepositoryAdapter implements TransactionLimitRepository {
    
    private final JpaTransactionLimitRepository jpaTransactionLimitRepository;

    public TransactionLimitRepositoryAdapter(JpaTransactionLimitRepository jpaTransactionLimitRepository) {
        this.jpaTransactionLimitRepository = jpaTransactionLimitRepository;
    }

    @Override
    public TransactionLimit save(TransactionLimit transactionLimit) {
        TransactionLimitEntity entity = toEntity(transactionLimit);
        TransactionLimitEntity saved = jpaTransactionLimitRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<TransactionLimit> findById(UUID id) {
        return jpaTransactionLimitRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<TransactionLimit> findByUserIdAndYearAndMonth(UUID userId, int year, int month) {
        return jpaTransactionLimitRepository.findByUserIdAndYearAndMonth(userId, year, month)
                .map(this::toDomain);
    }

    private TransactionLimitEntity toEntity(TransactionLimit transactionLimit) {
        TransactionLimitEntity entity = new TransactionLimitEntity();
        entity.setId(transactionLimit.getId());
        entity.setUserId(transactionLimit.getUserId());
        entity.setYear(transactionLimit.getYear());
        entity.setMonth(transactionLimit.getMonth());
        entity.setTransactionCount(transactionLimit.getTransactionCount());
        entity.setMaxAllowed(transactionLimit.getMaxAllowed());
        entity.setResetDate(transactionLimit.getResetDate());
        return entity;
    }

    private TransactionLimit toDomain(TransactionLimitEntity entity) {
        return new TransactionLimit(
                entity.getId(),
                entity.getUserId(),
                entity.getYear(),
                entity.getMonth(),
                entity.getTransactionCount(),
                entity.getMaxAllowed(),
                entity.getResetDate()
        );
    }
}

