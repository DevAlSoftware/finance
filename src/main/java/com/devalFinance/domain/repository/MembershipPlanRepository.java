package com.devalFinance.domain.repository;

import com.devalFinance.domain.model.MembershipPlan;
import com.devalFinance.domain.model.PlanType;

import java.util.Optional;
import java.util.UUID;

public interface MembershipPlanRepository {
    MembershipPlan save(MembershipPlan membershipPlan);
    Optional<MembershipPlan> findById(UUID id);
    Optional<MembershipPlan> findByPlanType(PlanType planType);
}


