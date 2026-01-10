package com.devalFinance.application.usecase.category;

import com.devalFinance.application.dto.request.CreateCategoryRequest;
import com.devalFinance.application.dto.response.CategoryResponse;
import com.devalFinance.domain.model.Category;
import com.devalFinance.domain.model.TransactionType;
import com.devalFinance.domain.repository.CategoryRepository;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CreateCategoryUseCase(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponse execute(CreateCategoryRequest request, UUID userId) {
        validateUserExists(userId);
        validateRequest(request);
        
        Category category = createCategory(request, userId);
        Category savedCategory = categoryRepository.save(category);
        
        return mapToResponse(savedCategory);
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private void validateRequest(CreateCategoryRequest request) {
        try {
            TransactionType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de transacción inválido. Debe ser INCOME o EXPENSE");
        }
    }

    private Category createCategory(CreateCategoryRequest request, UUID userId) {
        Category category = new Category();
        category.setUserId(userId);
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setType(TransactionType.valueOf(request.getType().toUpperCase()));
        category.setIsSystem(false);
        category.setIcon(request.getIcon() != null ? request.getIcon() : "📁");
        category.setColor(request.getColor() != null ? request.getColor() : "#6B7280");
        return category;
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getUserId(),
                category.getName(),
                category.getCode(),
                category.getType().name(),
                category.getIsSystem(),
                category.getParentCategoryId(),
                category.getIcon(),
                category.getColor(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}

