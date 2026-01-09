package com.devalFinance.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CreateTransactionRequest {
    
    @NotNull(message = "El ID de la cuenta es obligatorio")
    private UUID accountId;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal amount;
    
    @NotBlank(message = "El tipo de transacción es obligatorio")
    private String transactionType;
    
    private UUID categoryId;
    
    private String description;
    
    private LocalDate transactionDate;
    
    private List<String> tags;

    public CreateTransactionRequest() {
    }

    public CreateTransactionRequest(UUID accountId, BigDecimal amount, String transactionType,
                                   UUID categoryId, String description, LocalDate transactionDate,
                                   List<String> tags) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.categoryId = categoryId;
        this.description = description;
        this.transactionDate = transactionDate;
        this.tags = tags;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}


