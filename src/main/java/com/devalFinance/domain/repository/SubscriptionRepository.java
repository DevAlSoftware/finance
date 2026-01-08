package com.devalFinance.domain.repository;

import com.devalFinance.domain.model.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Optional<Subscription> findById(UUID id);
    Optional<Subscription> findByUserIdAndActive(UUID userId);
    List<Subscription> findByUserId(UUID userId);
}

