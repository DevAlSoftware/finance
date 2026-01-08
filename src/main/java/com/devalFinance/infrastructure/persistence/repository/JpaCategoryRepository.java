package com.devalFinance.infrastructure.persistence.repository;

import com.devalFinance.domain.model.TransactionType;
import com.devalFinance.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    List<CategoryEntity> findByUserIdOrIsSystem(UUID userId, Boolean isSystem);
    List<CategoryEntity> findByType(TransactionType type);
    List<CategoryEntity> findByIsSystemTrue();
    boolean existsByIdAndUserId(UUID id, UUID userId);
}

