package com.devalFinance.infrastructure.persistence.adapter;

import com.devalFinance.domain.model.Category;
import com.devalFinance.domain.model.TransactionType;
import com.devalFinance.domain.repository.CategoryRepository;
import com.devalFinance.infrastructure.persistence.entity.CategoryEntity;
import com.devalFinance.infrastructure.persistence.repository.JpaCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {
    
    private final JpaCategoryRepository jpaCategoryRepository;

    public CategoryRepositoryAdapter(JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity saved = jpaCategoryRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpaCategoryRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Category> findByUserIdOrIsSystem(UUID userId, boolean isSystem) {
        return jpaCategoryRepository.findByUserIdOrIsSystem(userId, isSystem).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByType(TransactionType type) {
        return jpaCategoryRepository.findByType(type).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findSystemCategories() {
        return jpaCategoryRepository.findByIsSystemTrue().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return jpaCategoryRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaCategoryRepository.deleteById(id);
    }

    private CategoryEntity toEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setUserId(category.getUserId());
        entity.setName(category.getName());
        entity.setCode(category.getCode());
        entity.setType(category.getType());
        entity.setIsSystem(category.getIsSystem());
        entity.setParentCategoryId(category.getParentCategoryId());
        entity.setCreatedAt(category.getCreatedAt());
        entity.setUpdatedAt(category.getUpdatedAt());
        return entity;
    }

    private Category toDomain(CategoryEntity entity) {
        return new Category(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getCode(),
                entity.getType(),
                entity.getIsSystem(),
                entity.getParentCategoryId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

