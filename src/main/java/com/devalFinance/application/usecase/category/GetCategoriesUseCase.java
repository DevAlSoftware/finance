package com.devalFinance.application.usecase.category;

import com.devalFinance.application.dto.response.CategoryResponse;
import com.devalFinance.domain.model.Category;
import com.devalFinance.domain.repository.CategoryRepository;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetCategoriesUseCase {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public GetCategoriesUseCase(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<CategoryResponse> execute(UUID userId) {
        validateUserExists(userId);
        
        // Obtener categorías del usuario y categorías del sistema
        List<Category> categories = categoryRepository.findByUserIdOrIsSystem(userId, true);
        
        return categories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
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

