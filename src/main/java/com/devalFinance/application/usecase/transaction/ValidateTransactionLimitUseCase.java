package com.devalFinance.application.usecase.transaction;

import com.devalFinance.domain.model.MembershipPlan;
import com.devalFinance.domain.repository.MembershipPlanRepository;
import com.devalFinance.domain.repository.SubscriptionRepository;
import com.devalFinance.domain.repository.TransactionLimitRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class ValidateTransactionLimitUseCase {
    
    private final TransactionLimitRepository transactionLimitRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ValidateTransactionLimitUseCase(TransactionLimitRepository transactionLimitRepository,
                                          MembershipPlanRepository membershipPlanRepository,
                                          SubscriptionRepository subscriptionRepository) {
        this.transactionLimitRepository = transactionLimitRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public void execute(UUID userId) {
        subscriptionRepository.findByUserIdAndActive(userId)
                .flatMap(subscription -> membershipPlanRepository.findById(subscription.getMembershipPlanId()))
                .ifPresent(plan -> validatePlanLimit(userId, plan));
    }

    private void validatePlanLimit(UUID userId, MembershipPlan plan) {
        if (!plan.isUnlimitedTransactions()) {
            LocalDate now = LocalDate.now();
            transactionLimitRepository.findByUserIdAndYearAndMonth(
                            userId, now.getYear(), now.getMonthValue())
                    .ifPresent(limit -> {
                        if (limit.hasReachedLimit()) {
                            throw new IllegalStateException(
                                    "Ha alcanzado el límite de " + plan.getMaxTransactionsPerMonth() +
                                    " transacciones mensuales. Actualice su plan para transacciones ilimitadas."
                            );
                        }
                    });
        }
    }
}

