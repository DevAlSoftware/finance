package com.devalFinance.application.usecase.account;

import com.devalFinance.application.dto.request.CreateAccountRequest;
import com.devalFinance.application.dto.response.AccountResponse;
import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.model.AccountType;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.domain.repository.MembershipPlanRepository;
import com.devalFinance.domain.repository.SubscriptionRepository;
import com.devalFinance.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class CreateAccountUseCase {
    
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    public CreateAccountUseCase(AccountRepository accountRepository,
                               UserRepository userRepository,
                               MembershipPlanRepository membershipPlanRepository,
                               SubscriptionRepository subscriptionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public AccountResponse execute(CreateAccountRequest request, UUID userId) {
        validateUserExists(userId);
        validateAccountLimit(userId);
        
        Account account = createAccount(request, userId);
        Account savedAccount = accountRepository.save(account);
        
        return mapToResponse(savedAccount);
    }

    private void validateUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private void validateAccountLimit(UUID userId) {
        subscriptionRepository.findByUserIdAndActive(userId)
                .flatMap(subscription -> membershipPlanRepository.findById(subscription.getMembershipPlanId()))
                .ifPresent(plan -> {
                    if (!plan.isUnlimitedAccounts()) {
                        List<Account> existingAccounts = accountRepository.findByUserIdAndActive(userId, true);
                        if (existingAccounts.size() >= plan.getMaxAccounts()) {
                            throw new IllegalStateException(
                                    "Ha alcanzado el límite de " + plan.getMaxAccounts() + " cuentas. " +
                                    "Actualice su plan para crear más cuentas."
                            );
                        }
                    }
                });
    }

    private Account createAccount(CreateAccountRequest request, UUID userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setName(request.getName());
        account.setAccountType(AccountType.valueOf(request.getAccountType().toUpperCase()));
        account.setInitialBalance(request.getInitialBalance() != null ? 
                request.getInitialBalance() : BigDecimal.ZERO);
        account.setCurrentBalance(account.getInitialBalance());
        account.setCurrency(request.getCurrency() != null ? request.getCurrency() : "COP");
        return account;
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUserId(),
                account.getName(),
                account.getAccountType().name(),
                account.getInitialBalance(),
                account.getCurrentBalance(),
                account.getCurrency(),
                account.getCreatedAt(),
                account.getActive()
        );
    }
}


