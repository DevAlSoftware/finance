package com.devalFinance.infrastructure.persistence.adapter;

import com.devalFinance.domain.model.MembershipPlan;
import com.devalFinance.domain.model.PlanType;
import com.devalFinance.domain.repository.MembershipPlanRepository;
import com.devalFinance.infrastructure.persistence.entity.MembershipPlanEntity;
import com.devalFinance.infrastructure.persistence.repository.JpaMembershipPlanRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MembershipPlanRepositoryAdapter implements MembershipPlanRepository {
    
    private final JpaMembershipPlanRepository jpaMembershipPlanRepository;

    public MembershipPlanRepositoryAdapter(JpaMembershipPlanRepository jpaMembershipPlanRepository) {
        this.jpaMembershipPlanRepository = jpaMembershipPlanRepository;
    }

    @Override
    public MembershipPlan save(MembershipPlan membershipPlan) {
        MembershipPlanEntity entity = toEntity(membershipPlan);
        MembershipPlanEntity saved = jpaMembershipPlanRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<MembershipPlan> findById(UUID id) {
        return jpaMembershipPlanRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<MembershipPlan> findByPlanType(PlanType planType) {
        return jpaMembershipPlanRepository.findByPlanType(planType)
                .map(this::toDomain);
    }

    private MembershipPlanEntity toEntity(MembershipPlan membershipPlan) {
        MembershipPlanEntity entity = new MembershipPlanEntity();
        entity.setId(membershipPlan.getId());
        entity.setPlanType(membershipPlan.getPlanType());
        entity.setMaxAccounts(membershipPlan.getMaxAccounts());
        entity.setMaxTransactionsPerMonth(membershipPlan.getMaxTransactionsPerMonth());
        entity.setHasAdvancedReports(membershipPlan.getHasAdvancedReports());
        entity.setHasExportCapabilities(membershipPlan.getHasExportCapabilities());
        entity.setHasHistoricalData(membershipPlan.getHasHistoricalData());
        entity.setHistoricalDataYears(membershipPlan.getHistoricalDataYears());
        entity.setHasAutoCategorization(membershipPlan.getHasAutoCategorization());
        entity.setHasCustomCategories(membershipPlan.getHasCustomCategories());
        entity.setHasMultipleUsers(membershipPlan.getHasMultipleUsers());
        entity.setHasApiAccess(membershipPlan.getHasApiAccess());
        return entity;
    }

    private MembershipPlan toDomain(MembershipPlanEntity entity) {
        return new MembershipPlan(
                entity.getId(),
                entity.getPlanType(),
                entity.getMaxAccounts(),
                entity.getMaxTransactionsPerMonth(),
                entity.getHasAdvancedReports(),
                entity.getHasExportCapabilities(),
                entity.getHasHistoricalData(),
                entity.getHistoricalDataYears(),
                entity.getHasAutoCategorization(),
                entity.getHasCustomCategories(),
                entity.getHasMultipleUsers(),
                entity.getHasApiAccess()
        );
    }
}

