package com.devalFinance.infrastructure.persistence.repository;

import com.devalFinance.domain.model.PlanType;
import com.devalFinance.infrastructure.persistence.entity.MembershipPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaMembershipPlanRepository extends JpaRepository<MembershipPlanEntity, UUID> {
    Optional<MembershipPlanEntity> findByPlanType(PlanType planType);
}

