package com.devalFinance.infrastructure.persistence.repository;

import com.devalFinance.infrastructure.persistence.entity.TransactionLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTransactionLimitRepository extends JpaRepository<TransactionLimitEntity, UUID> {
    Optional<TransactionLimitEntity> findByUserIdAndYearAndMonth(UUID userId, Integer year, Integer month);
}

