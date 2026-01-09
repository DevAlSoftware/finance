package com.devalFinance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {
    private UUID id;
    private UUID userId;
    private String name;
    private AccountType accountType;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;

    public Account() {
        this.currency = "COP";
        this.active = true;
        this.initialBalance = BigDecimal.ZERO;
        this.currentBalance = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Account(UUID id, UUID userId, String name, AccountType accountType, BigDecimal initialBalance,
                   BigDecimal currentBalance, String currency, LocalDateTime createdAt, LocalDateTime updatedAt,
                   Boolean active) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.accountType = accountType;
        this.initialBalance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
        this.currentBalance = currentBalance != null ? currentBalance : this.initialBalance;
        this.currency = currency != null ? currency : "COP";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active != null ? active : true;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    public void updateBalance(BigDecimal amount) {
        if (this.currentBalance == null) {
            this.currentBalance = BigDecimal.ZERO;
        }
        this.currentBalance = this.currentBalance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
}


