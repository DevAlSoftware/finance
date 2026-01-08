package com.devalFinance.infrastructure.persistence.repository;

import com.devalFinance.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByUserId(UUID userId);
    List<TransactionEntity> findByAccountId(UUID accountId);
    List<TransactionEntity> findByUserIdAndTransactionDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT t FROM TransactionEntity t WHERE t.userId = :userId AND YEAR(t.transactionDate) = :year")
    List<TransactionEntity> findByUserIdAndYear(@Param("userId") UUID userId, @Param("year") int year);
    
    @Query("SELECT COUNT(t) FROM TransactionEntity t WHERE t.userId = :userId AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    long countByUserIdAndYearAndMonth(@Param("userId") UUID userId, @Param("year") int year, @Param("month") int month);
    
    boolean existsByIdAndUserId(UUID id, UUID userId);
}

