package com.devalFinance.domain.repository;

import com.devalFinance.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    List<Account> findByUserId(UUID userId);
    List<Account> findByUserIdAndActive(UUID userId, boolean active);
    void deleteById(UUID id);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}


