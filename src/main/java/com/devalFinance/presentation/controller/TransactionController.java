package com.devalFinance.presentation.controller;

import com.devalFinance.application.dto.request.CreateTransactionRequest;
import com.devalFinance.application.dto.response.TransactionResponse;
import com.devalFinance.application.usecase.transaction.CreateTransactionUseCase;
import com.devalFinance.application.usecase.transaction.GetTransactionsUseCase;
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
public class TransactionController {
    
    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
                                GetTransactionsUseCase getTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request,
            @CurrentUser UUID userId) {
        TransactionResponse response = createTransactionUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @CurrentUser UUID userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionResponse> transactions = getTransactionsUseCase.execute(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}

