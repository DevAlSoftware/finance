package com.devalFinance.application.usecase.category;

import com.devalFinance.domain.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteCategoryUseCase {
    
    private final CategoryRepository categoryRepository;

    public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void execute(UUID categoryId, UUID userId) {
        // Verificar que la categoría existe y pertenece al usuario
        if (!categoryRepository.existsByIdAndUserId(categoryId, userId)) {
            categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            throw new IllegalArgumentException("No tiene permisos para eliminar esta categoría");
        }
        
        // No permitir eliminar categorías del sistema
        categoryRepository.findById(categoryId)
                .ifPresent(category -> {
                    if (Boolean.TRUE.equals(category.getIsSystem())) {
                        throw new IllegalStateException("No se pueden eliminar categorías del sistema");
                    }
                });
        
        categoryRepository.deleteById(categoryId);
    }
}

