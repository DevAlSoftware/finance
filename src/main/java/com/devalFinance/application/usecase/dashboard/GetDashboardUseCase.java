package com.devalFinance.application.usecase.dashboard;

import com.devalFinance.application.dto.response.DashboardResponse;
import com.devalFinance.application.dto.response.TransactionResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.model.Transaction;
import com.devalFinance.domain.model.TransactionType;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GetDashboardUseCase {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public GetDashboardUseCase(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public DashboardResponse execute(UUID userId) {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());
        
        List<Account> accounts = accountRepository.findByUserIdAndActive(userId, true);
        List<Transaction> monthlyTransactions = transactionRepository
                .findByUserIdAndTransactionDateBetween(userId, monthStart, monthEnd);
        
        BigDecimal totalBalance = calculateTotalBalance(accounts);
        BigDecimal monthlyIncome = calculateMonthlyIncome(monthlyTransactions);
        BigDecimal monthlyExpenses = calculateMonthlyExpenses(monthlyTransactions);
        BigDecimal monthlyBalance = monthlyIncome.subtract(monthlyExpenses);
        
        int transactionCount = monthlyTransactions.size();
        
        List<DashboardResponse.AccountSummary> accountSummaries = accounts.stream()
                .map(this::mapToAccountSummary)
                .collect(Collectors.toList());
        
        List<TransactionResponse> recentTransactions = monthlyTransactions.stream()
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .limit(10)
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
        
        return new DashboardResponse(
                totalBalance,
                monthlyIncome,
                monthlyExpenses,
                monthlyBalance,
                transactionCount,
                monthStart,
                monthEnd,
                accountSummaries,
                recentTransactions
        );
    }

    private BigDecimal calculateTotalBalance(List<Account> accounts) {
        return accounts.stream()
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private DashboardResponse.AccountSummary mapToAccountSummary(Account account) {
        return new DashboardResponse.AccountSummary(
                account.getId().toString(),
                account.getName(),
                account.getAccountType().name(),
                account.getCurrentBalance(),
                account.getCurrency()
        );
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getUserId(),
                transaction.getAmount(),
                transaction.getTransactionType().name(),
                transaction.getCategoryId(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getTags(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}

