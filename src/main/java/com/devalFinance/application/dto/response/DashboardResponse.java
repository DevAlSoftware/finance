package com.devalFinance.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DashboardResponse {
    
    private BigDecimal totalBalance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal monthlyBalance;
    private int monthlyTransactionCount;
    private LocalDate monthStart;
    private LocalDate monthEnd;
    private List<AccountSummary> accounts;
    private List<TransactionResponse> recentTransactions;

    public DashboardResponse() {
    }

    public DashboardResponse(BigDecimal totalBalance, BigDecimal monthlyIncome, BigDecimal monthlyExpenses,
                           BigDecimal monthlyBalance, int monthlyTransactionCount, LocalDate monthStart,
                           LocalDate monthEnd, List<AccountSummary> accounts,
                           List<TransactionResponse> recentTransactions) {
        this.totalBalance = totalBalance;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
        this.monthlyBalance = monthlyBalance;
        this.monthlyTransactionCount = monthlyTransactionCount;
        this.monthStart = monthStart;
        this.monthEnd = monthEnd;
        this.accounts = accounts;
        this.recentTransactions = recentTransactions;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public BigDecimal getMonthlyBalance() {
        return monthlyBalance;
    }

    public void setMonthlyBalance(BigDecimal monthlyBalance) {
        this.monthlyBalance = monthlyBalance;
    }

    public int getMonthlyTransactionCount() {
        return monthlyTransactionCount;
    }

    public void setMonthlyTransactionCount(int monthlyTransactionCount) {
        this.monthlyTransactionCount = monthlyTransactionCount;
    }

    public LocalDate getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(LocalDate monthStart) {
        this.monthStart = monthStart;
    }

    public LocalDate getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(LocalDate monthEnd) {
        this.monthEnd = monthEnd;
    }

    public List<AccountSummary> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountSummary> accounts) {
        this.accounts = accounts;
    }

    public List<TransactionResponse> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<TransactionResponse> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    public static class AccountSummary {
        private String id;
        private String name;
        private String accountType;
        private BigDecimal currentBalance;
        private String currency;

        public AccountSummary() {
        }

        public AccountSummary(String id, String name, String accountType, BigDecimal currentBalance, String currency) {
            this.id = id;
            this.name = name;
            this.accountType = accountType;
            this.currentBalance = currentBalance;
            this.currency = currency;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}


