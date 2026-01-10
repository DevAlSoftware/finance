package com.devalFinance.presentation.controller;

import com.devalFinance.application.dto.request.CreateCategoryRequest;
import com.devalFinance.application.dto.response.CategoryResponse;
import com.devalFinance.application.usecase.category.CreateCategoryUseCase;
import com.devalFinance.application.usecase.category.DeleteCategoryUseCase;
import com.devalFinance.application.usecase.category.GetCategoriesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categorías", description = "API para gestión de categorías de transacciones")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {
    
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(GetCategoriesUseCase getCategoriesUseCase,
                             CreateCategoryUseCase createCategoryUseCase,
                             DeleteCategoryUseCase deleteCategoryUseCase) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.createCategoryUseCase = createCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Obtiene todas las categorías del usuario y categorías del sistema")
    public ResponseEntity<List<CategoryResponse>> getCategories(@CurrentUser UUID userId) {
        List<CategoryResponse> categories = getCategoriesUseCase.execute(userId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría personalizada para el usuario")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            @CurrentUser UUID userId) {
        CategoryResponse response = createCategoryUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría personalizada del usuario")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID categoryId,
            @CurrentUser UUID userId) {
        deleteCategoryUseCase.execute(categoryId, userId);
        return ResponseEntity.noContent().build();
    }
}

