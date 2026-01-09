package com.devalFinance.infrastructure.persistence.adapter;

import com.devalFinance.domain.model.Transaction;
import com.devalFinance.domain.repository.TransactionRepository;
import com.devalFinance.infrastructure.persistence.entity.TransactionEntity;
import com.devalFinance.infrastructure.persistence.repository.JpaTransactionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {
    
    private final JpaTransactionRepository jpaTransactionRepository;

    public TransactionRepositoryAdapter(JpaTransactionRepository jpaTransactionRepository) {
        this.jpaTransactionRepository = jpaTransactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = toEntity(transaction);
        TransactionEntity saved = jpaTransactionRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaTransactionRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Transaction> findByUserId(UUID userId) {
        return jpaTransactionRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return jpaTransactionRepository.findByAccountId(accountId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByUserIdAndTransactionDateBetween(UUID userId, LocalDate startDate, LocalDate endDate) {
        return jpaTransactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByUserIdAndYear(UUID userId, int year) {
        return jpaTransactionRepository.findByUserIdAndYear(userId, year).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countByUserIdAndYearAndMonth(UUID userId, int year, int month) {
        return jpaTransactionRepository.countByUserIdAndYearAndMonth(userId, year, month);
    }

    @Override
    public void deleteById(UUID id) {
        jpaTransactionRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return jpaTransactionRepository.existsByIdAndUserId(id, userId);
    }

    private TransactionEntity toEntity(Transaction transaction) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.getId());
        entity.setAccountId(transaction.getAccountId());
        entity.setUserId(transaction.getUserId());
        entity.setAmount(transaction.getAmount());
        entity.setTransactionType(transaction.getTransactionType());
        entity.setCategoryId(transaction.getCategoryId());
        entity.setDescription(transaction.getDescription());
        entity.setTransactionDate(transaction.getTransactionDate());
        entity.setTags(transaction.getTags());
        entity.setCreatedAt(transaction.getCreatedAt());
        entity.setUpdatedAt(transaction.getUpdatedAt());
        return entity;
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAccountId(),
                entity.getUserId(),
                entity.getAmount(),
                entity.getTransactionType(),
                entity.getCategoryId(),
                entity.getDescription(),
                entity.getTransactionDate(),
                entity.getTags(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}


