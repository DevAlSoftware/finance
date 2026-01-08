package com.devalFinance.presentation.controller;

import com.devalFinance.application.dto.request.CreateTransactionRequest;
import com.devalFinance.application.dto.request.CreateTransferRequest;
import com.devalFinance.application.dto.response.TransactionResponse;
import com.devalFinance.application.dto.response.TransferResponse;
import com.devalFinance.application.usecase.transaction.CreateTransactionUseCase;
import com.devalFinance.application.usecase.transaction.CreateTransferUseCase;
import com.devalFinance.application.usecase.transaction.GetTransactionsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transacciones", description = "API para gestión de transacciones financieras")
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionController {
    
    private final CreateTransactionUseCase createTransactionUseCase;
    private final CreateTransferUseCase createTransferUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
                                CreateTransferUseCase createTransferUseCase,
                                GetTransactionsUseCase getTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.createTransferUseCase = createTransferUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
    }

    @PostMapping
    @Operation(summary = "Crear transacción", description = "Crea una nueva transacción (INGRESO o GASTO) y actualiza el saldo de la cuenta")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request,
            @CurrentUser UUID userId) {
        TransactionResponse response = createTransactionUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Crear transferencia", description = "Transfiere dinero entre dos cuentas del usuario. Crea dos transacciones vinculadas (EXPENSE en origen, INCOME en destino)")
    public ResponseEntity<TransferResponse> createTransfer(
            @Valid @RequestBody CreateTransferRequest request,
            @CurrentUser UUID userId) {
        TransferResponse response = createTransferUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar transacciones", description = "Obtiene todas las transacciones del usuario, opcionalmente filtradas por fecha")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @CurrentUser UUID userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionResponse> transactions = getTransactionsUseCase.execute(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}
