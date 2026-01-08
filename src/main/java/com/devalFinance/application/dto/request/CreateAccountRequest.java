package com.devalFinance.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class CreateAccountRequest {
    
    @NotBlank(message = "El nombre de la cuenta es obligatorio")
    private String name;
    
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String accountType;
    
    @PositiveOrZero(message = "El saldo inicial debe ser mayor o igual a cero")
    private BigDecimal initialBalance;
    
    private String currency;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String name, String accountType, BigDecimal initialBalance, String currency) {
        this.name = name;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

