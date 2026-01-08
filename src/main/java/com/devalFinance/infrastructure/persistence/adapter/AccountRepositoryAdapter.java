package com.devalFinance.infrastructure.persistence.adapter;

import com.devalFinance.domain.model.Account;
import com.devalFinance.domain.repository.AccountRepository;
import com.devalFinance.infrastructure.persistence.entity.AccountEntity;
import com.devalFinance.infrastructure.persistence.repository.JpaAccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccountRepositoryAdapter implements AccountRepository {
    
    private final JpaAccountRepository jpaAccountRepository;

    public AccountRepositoryAdapter(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = toEntity(account);
        AccountEntity saved = jpaAccountRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaAccountRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return jpaAccountRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> findByUserIdAndActive(UUID userId, boolean active) {
        return jpaAccountRepository.findByUserIdAndActive(userId, active).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaAccountRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return jpaAccountRepository.existsByIdAndUserId(id, userId);
    }

    private AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setId(account.getId());
        entity.setUserId(account.getUserId());
        entity.setName(account.getName());
        entity.setAccountType(account.getAccountType());
        entity.setInitialBalance(account.getInitialBalance());
        entity.setCurrentBalance(account.getCurrentBalance());
        entity.setCurrency(account.getCurrency());
        entity.setCreatedAt(account.getCreatedAt());
        entity.setUpdatedAt(account.getUpdatedAt());
        entity.setActive(account.getActive());
        return entity;
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getAccountType(),
                entity.getInitialBalance(),
                entity.getCurrentBalance(),
                entity.getCurrency(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getActive()
        );
    }
}

