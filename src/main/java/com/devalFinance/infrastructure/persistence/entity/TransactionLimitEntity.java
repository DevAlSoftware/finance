package com.devalFinance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction_limits", indexes = {
    @Index(name = "idx_limit_user_year_month", columnList = "user_id, year, month", unique = true)
})
public class TransactionLimitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(name = "transaction_count", nullable = false)
    private Integer transactionCount;

    @Column(name = "max_allowed", nullable = false)
    private Integer maxAllowed;

    @Column(name = "reset_date")
    private LocalDateTime resetDate;

    @PrePersist
    protected void onCreate() {
        if (transactionCount == null) {
            transactionCount = 0;
        }
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
}

