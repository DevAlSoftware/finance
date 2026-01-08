package com.devalFinance.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransferResponse {
    
    private UUID transferId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private List<String> tags;
    private TransactionResponse fromTransaction;
    private TransactionResponse toTransaction;
    private LocalDateTime createdAt;

    public TransferResponse() {
    }

    public TransferResponse(UUID transferId, UUID fromAccountId, UUID toAccountId, BigDecimal amount,
                           String description, LocalDate transactionDate, List<String> tags,
                           TransactionResponse fromTransaction, TransactionResponse toTransaction,
                           LocalDateTime createdAt) {
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.tags = tags;
        this.fromTransaction = fromTransaction;
        this.toTransaction = toTransaction;
        this.createdAt = createdAt;
    }

    public UUID getTransferId() {
        return transferId;
    }

    public void setTransferId(UUID transferId) {
        this.transferId = transferId;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(UUID fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(UUID toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public TransactionResponse getFromTransaction() {
        return fromTransaction;
    }

    public void setFromTransaction(TransactionResponse fromTransaction) {
        this.fromTransaction = fromTransaction;
    }

    public TransactionResponse getToTransaction() {
        return toTransaction;
    }

    public void setToTransaction(TransactionResponse toTransaction) {
        this.toTransaction = toTransaction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

