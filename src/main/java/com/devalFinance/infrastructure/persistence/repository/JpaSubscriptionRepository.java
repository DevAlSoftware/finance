package com.devalFinance.infrastructure.persistence.repository;

import com.devalFinance.domain.model.SubscriptionStatus;
import com.devalFinance.infrastructure.persistence.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
    List<SubscriptionEntity> findByUserId(UUID userId);
    
    @Query("SELECT s FROM SubscriptionEntity s WHERE s.userId = :userId AND s.status = :status AND (s.endDate IS NULL OR s.endDate > :now)")
    Optional<SubscriptionEntity> findByUserIdAndStatusAndActive(@Param("userId") UUID userId, @Param("status") SubscriptionStatus status, @Param("now") LocalDateTime now);
}

