package com.devalFinance.application.usecase.transaction;

import com.devalFinance.application.dto.request.CreateTransactionRequest;
import com.devalFinance.application.dto.response.TransactionResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.model.Transaction;
import com.devalFinance.domain.model.TransactionLimit;
import com.devalFinance.domain.model.TransactionType;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.MembershipPlanRepository;
import com.devalFinance.domain.repository.SubscriptionRepository;
import com.devalFinance.domain.repository.TransactionLimitRepository;
import com.devalFinance.domain.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class CreateTransactionUseCase {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionLimitRepository transactionLimitRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    public CreateTransactionUseCase(TransactionRepository transactionRepository,
                                   AccountRepository accountRepository,
                                   TransactionLimitRepository transactionLimitRepository,
                                   MembershipPlanRepository membershipPlanRepository,
                                   SubscriptionRepository subscriptionRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionLimitRepository = transactionLimitRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public TransactionResponse execute(CreateTransactionRequest request, UUID userId) {
        validateAccount(request.getAccountId(), userId);
        validateTransactionLimit(userId);
        
        Transaction transaction = createTransaction(request, userId);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        updateAccountBalance(request.getAccountId(), savedTransaction);
        incrementTransactionCount(userId);
        
        return mapToResponse(savedTransaction);
    }

    private void validateAccount(UUID accountId, UUID userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        
        if (!account.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para usar esta cuenta");
        }
        
        if (!account.isActive()) {
            throw new IllegalStateException("La cuenta está inactiva");
        }
    }

    private void validateTransactionLimit(UUID userId) {
        subscriptionRepository.findByUserIdAndActive(userId)
                .flatMap(subscription -> membershipPlanRepository.findById(subscription.getMembershipPlanId()))
                .ifPresent(plan -> {
                    if (!plan.isUnlimitedTransactions()) {
                        LocalDate now = LocalDate.now();
                        TransactionLimit limit = transactionLimitRepository
                                .findByUserIdAndYearAndMonth(userId, now.getYear(), now.getMonthValue())
                                .orElseGet(() -> createNewLimit(userId, now, plan.getMaxTransactionsPerMonth()));
                        
                        if (limit.hasReachedLimit()) {
                            throw new IllegalStateException(
                                    "Ha alcanzado el límite de " + plan.getMaxTransactionsPerMonth() +
                                    " transacciones mensuales. Actualice su plan para transacciones ilimitadas."
                            );
                        }
                    }
                });
    }

    private TransactionLimit createNewLimit(UUID userId, LocalDate date, Integer maxAllowed) {
        TransactionLimit limit = new TransactionLimit();
        limit.setUserId(userId);
        limit.setYear(date.getYear());
        limit.setMonth(date.getMonthValue());
        limit.setMaxAllowed(maxAllowed);
        limit.setTransactionCount(0);
        return transactionLimitRepository.save(limit);
    }

    private Transaction createTransaction(CreateTransactionRequest request, UUID userId) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(request.getAccountId());
        transaction.setUserId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.valueOf(request.getTransactionType().toUpperCase()));
        transaction.setCategoryId(request.getCategoryId());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate() != null ? 
                request.getTransactionDate() : LocalDate.now());
        transaction.setTags(request.getTags());
        return transaction;
    }

    private void updateAccountBalance(UUID accountId, Transaction transaction) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Cuenta no encontrada"));
        
        account.updateBalance(transaction.getSignedAmount());
        accountRepository.save(account);
    }

    private void incrementTransactionCount(UUID userId) {
        LocalDate now = LocalDate.now();
        transactionLimitRepository.findByUserIdAndYearAndMonth(userId, now.getYear(), now.getMonthValue())
                .ifPresent(limit -> {
                    limit.incrementCount();
                    transactionLimitRepository.save(limit);
                });
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
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


