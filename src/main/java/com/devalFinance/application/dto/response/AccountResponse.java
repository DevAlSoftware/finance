package com.devalFinance.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private String accountType;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private String currency;
    private LocalDateTime createdAt;
    private Boolean active;

    public AccountResponse() {
    }

    public AccountResponse(UUID id, UUID userId, String name, String accountType,
                           BigDecimal initialBalance, BigDecimal currentBalance,
                           String currency, LocalDateTime createdAt, Boolean active) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
        this.currency = currency;
        this.createdAt = createdAt;
        this.active = active;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}


