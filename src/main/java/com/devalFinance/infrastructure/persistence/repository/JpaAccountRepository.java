package com.devalFinance.infrastructure.persistence.repository;

import com.devalFinance.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findByUserId(UUID userId);
    List<AccountEntity> findByUserIdAndActive(UUID userId, Boolean active);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}

