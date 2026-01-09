package com.devalFinance.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionLimit {
    private UUID id;
    private UUID userId;
    private Integer year;
    private Integer month;
    private Integer transactionCount;
    private Integer maxAllowed;
    private LocalDateTime resetDate;

    public TransactionLimit() {
        this.transactionCount = 0;
    }

    public TransactionLimit(UUID id, UUID userId, Integer year, Integer month, Integer transactionCount,
                             Integer maxAllowed, LocalDateTime resetDate) {
        this.id = id;
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.transactionCount = transactionCount != null ? transactionCount : 0;
        this.maxAllowed = maxAllowed;
        this.resetDate = resetDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Integer getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(Integer maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public LocalDateTime getResetDate() {
        return resetDate;
    }

    public void setResetDate(LocalDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public void incrementCount() {
        this.transactionCount++;
    }

    public boolean hasReachedLimit() {
        return maxAllowed != null && transactionCount >= maxAllowed;
    }

    public int getRemainingTransactions() {
        if (maxAllowed == null) {
            return Integer.MAX_VALUE;
        }
        return Math.max(0, maxAllowed - transactionCount);
    }
}


