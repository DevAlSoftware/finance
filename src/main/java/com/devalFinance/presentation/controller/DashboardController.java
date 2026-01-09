package com.devalFinance.presentation.controller;

import com.devalFinance.application.dto.response.DashboardResponse;
import com.devalFinance.application.usecase.dashboard.GetDashboardUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "API para obtener resumen financiero del usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {
    
    private final GetDashboardUseCase getDashboardUseCase;

    public DashboardController(GetDashboardUseCase getDashboardUseCase) {
        this.getDashboardUseCase = getDashboardUseCase;
    }

    @GetMapping
    @Operation(summary = "Obtener dashboard", description = "Obtiene el resumen financiero del usuario: saldos totales, ingresos y gastos del mes actual, y últimas transacciones")
    public ResponseEntity<DashboardResponse> getDashboard(@CurrentUser UUID userId) {
        DashboardResponse response = getDashboardUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }
}


