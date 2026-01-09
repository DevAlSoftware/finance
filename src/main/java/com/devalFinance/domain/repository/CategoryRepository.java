package com.devalFinance.domain.repository;

import com.devalFinance.domain.model.Category;
import com.devalFinance.domain.model.TransactionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(UUID id);
    List<Category> findByUserIdOrIsSystem(UUID userId, boolean isSystem);
    List<Category> findByType(TransactionType type);
    List<Category> findSystemCategories();
    boolean existsByIdAndUserId(UUID id, UUID userId);
    void deleteById(UUID id);
}


