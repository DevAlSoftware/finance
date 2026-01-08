package com.devalFinance.infrastructure.persistence.adapter;

import com.devalFinance.domain.model.Subscription;
import com.devalFinance.domain.repository.SubscriptionRepository;
import com.devalFinance.infrastructure.persistence.entity.SubscriptionEntity;
import com.devalFinance.infrastructure.persistence.repository.JpaSubscriptionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {
    
    private final JpaSubscriptionRepository jpaSubscriptionRepository;

    public SubscriptionRepositoryAdapter(JpaSubscriptionRepository jpaSubscriptionRepository) {
        this.jpaSubscriptionRepository = jpaSubscriptionRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity entity = toEntity(subscription);
        SubscriptionEntity saved = jpaSubscriptionRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        return jpaSubscriptionRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Subscription> findByUserIdAndActive(UUID userId) {
        return jpaSubscriptionRepository.findByUserIdAndStatusAndActive(
                userId,
                com.devalFinance.domain.model.SubscriptionStatus.ACTIVE,
                LocalDateTime.now()
        ).map(this::toDomain);
    }

    @Override
    public List<Subscription> findByUserId(UUID userId) {
        return jpaSubscriptionRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SubscriptionEntity toEntity(Subscription subscription) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setId(subscription.getId());
        entity.setUserId(subscription.getUserId());
        entity.setMembershipPlanId(subscription.getMembershipPlanId());
        entity.setStartDate(subscription.getStartDate());
        entity.setEndDate(subscription.getEndDate());
        entity.setStatus(subscription.getStatus());
        entity.setCreatedAt(subscription.getCreatedAt());
        entity.setUpdatedAt(subscription.getUpdatedAt());
        return entity;
    }

    private Subscription toDomain(SubscriptionEntity entity) {
        return new Subscription(
                entity.getId(),
                entity.getUserId(),
                entity.getMembershipPlanId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

