package com.devalFinance.application.usecase.transaction;

import com.devalFinance.application.dto.request.CreateTransferRequest;
import com.devalFinance.application.dto.response.TransferResponse;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CreateTransferUseCase {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionLimitRepository transactionLimitRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    public CreateTransferUseCase(TransactionRepository transactionRepository,
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
    public TransferResponse execute(CreateTransferRequest request, UUID userId) {
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de origen no encontrada"));
        
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de destino no encontrada"));
        
        validateTransferRequest(request, userId, fromAccount, toAccount);
        validateTransactionLimitForTransfer(userId);
        
        LocalDate transactionDate = request.getTransactionDate() != null ? 
                request.getTransactionDate() : LocalDate.now();
        
        Transaction fromTransaction = createFromTransaction(request, userId, transactionDate, toAccount.getName());
        Transaction toTransaction = createToTransaction(request, userId, transactionDate, fromAccount.getName());
        
        Transaction savedFromTransaction = transactionRepository.save(fromTransaction);
        Transaction savedToTransaction = transactionRepository.save(toTransaction);
        
        updateAccountBalances(fromAccount, toAccount, savedFromTransaction, savedToTransaction);
        incrementTransactionCountTwice(userId);
        
        return mapToTransferResponse(request, savedFromTransaction, savedToTransaction, transactionDate);
    }

    private void validateTransferRequest(CreateTransferRequest request, UUID userId, Account fromAccount, Account toAccount) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("La cuenta de origen y destino no pueden ser la misma");
        }
        
        if (!fromAccount.getUserId().equals(userId) || !toAccount.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para usar una o ambas cuentas");
        }
        
        if (!fromAccount.isActive() || !toAccount.isActive()) {
            throw new IllegalStateException("Una o ambas cuentas están inactivas");
        }
        
        if (fromAccount.getCurrentBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalStateException("La cuenta de origen no tiene saldo suficiente");
        }
    }

    private void validateTransactionLimitForTransfer(UUID userId) {
        subscriptionRepository.findByUserIdAndActive(userId)
                .flatMap(subscription -> membershipPlanRepository.findById(subscription.getMembershipPlanId()))
                .ifPresent(plan -> {
                    if (!plan.isUnlimitedTransactions()) {
                        LocalDate now = LocalDate.now();
                        TransactionLimit limit = transactionLimitRepository
                                .findByUserIdAndYearAndMonth(userId, now.getYear(), now.getMonthValue())
                                .orElseGet(() -> createNewLimit(userId, now, plan.getMaxTransactionsPerMonth()));
                        
                        if (limit.getTransactionCount() + 2 > limit.getMaxAllowed()) {
                            throw new IllegalStateException(
                                    "Ha alcanzado el límite de " + plan.getMaxTransactionsPerMonth() +
                                    " transacciones mensuales. Una transferencia consume 2 transacciones. Actualice su plan para transacciones ilimitadas."
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

    private Transaction createFromTransaction(CreateTransferRequest request, UUID userId, LocalDate transactionDate, String toAccountName) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(request.getFromAccountId());
        transaction.setUserId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.EXPENSE);
        transaction.setDescription(buildTransferDescription(request.getDescription(), "Transferencia enviada a " + toAccountName));
        transaction.setTransactionDate(transactionDate);
        
        List<String> tags = new ArrayList<>();
        if (request.getTags() != null) {
            tags.addAll(request.getTags());
        }
        tags.add("transferencia");
        tags.add("envio");
        transaction.setTags(tags);
        
        return transaction;
    }

    private Transaction createToTransaction(CreateTransferRequest request, UUID userId, LocalDate transactionDate, String fromAccountName) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(request.getToAccountId());
        transaction.setUserId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.INCOME);
        transaction.setDescription(buildTransferDescription(request.getDescription(), "Transferencia recibida de " + fromAccountName));
        transaction.setTransactionDate(transactionDate);
        
        List<String> tags = new ArrayList<>();
        if (request.getTags() != null) {
            tags.addAll(request.getTags());
        }
        tags.add("transferencia");
        tags.add("recepcion");
        transaction.setTags(tags);
        
        return transaction;
    }

    private String buildTransferDescription(String userDescription, String prefix) {
        if (userDescription != null && !userDescription.trim().isEmpty()) {
            return prefix + ": " + userDescription;
        }
        return prefix;
    }

    private void updateAccountBalances(Account fromAccount, Account toAccount, 
                                      Transaction fromTransaction, Transaction toTransaction) {
        fromAccount.updateBalance(fromTransaction.getSignedAmount());
        toAccount.updateBalance(toTransaction.getSignedAmount());
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private void incrementTransactionCountTwice(UUID userId) {
        LocalDate now = LocalDate.now();
        transactionLimitRepository.findByUserIdAndYearAndMonth(userId, now.getYear(), now.getMonthValue())
                .ifPresent(limit -> {
                    limit.incrementCount();
                    limit.incrementCount();
                    transactionLimitRepository.save(limit);
                });
    }

    private TransferResponse mapToTransferResponse(CreateTransferRequest request, 
                                                   Transaction fromTransaction, 
                                                   Transaction toTransaction,
                                                   LocalDate transactionDate) {
        TransactionResponse fromResponse = mapToTransactionResponse(fromTransaction);
        TransactionResponse toResponse = mapToTransactionResponse(toTransaction);
        
        return new TransferResponse(
                fromTransaction.getId(),
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                request.getDescription(),
                transactionDate,
                request.getTags(),
                fromResponse,
                toResponse,
                LocalDateTime.now()
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

