package com.devalFinance.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Subscription {
    private UUID id;
    private UUID userId;
    private UUID membershipPlanId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Subscription() {
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Subscription(UUID id, UUID userId, UUID membershipPlanId, LocalDateTime startDate,
                        LocalDateTime endDate, SubscriptionStatus status, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.membershipPlanId = membershipPlanId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status != null ? status : SubscriptionStatus.ACTIVE;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getMembershipPlanId() {
        return membershipPlanId;
    }

    public void setMembershipPlanId(UUID membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE && (endDate == null || endDate.isAfter(LocalDateTime.now()));
    }

    public boolean isExpired() {
        return endDate != null && endDate.isBefore(LocalDateTime.now());
    }
}


