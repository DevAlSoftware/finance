package com.devalFinance.infrastructure.persistence.entity;

import com.devalFinance.domain.model.PlanType;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "membership_plans", indexes = {
    @Index(name = "idx_plan_type", columnList = "plan_type", unique = true)
})
public class MembershipPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, unique = true, length = 20)
    private PlanType planType;

    @Column(name = "max_accounts")
    private Integer maxAccounts;

    @Column(name = "max_transactions_per_month")
    private Integer maxTransactionsPerMonth;

    @Column(name = "has_advanced_reports", nullable = false)
    private Boolean hasAdvancedReports;

    @Column(name = "has_export_capabilities", nullable = false)
    private Boolean hasExportCapabilities;

    @Column(name = "has_historical_data", nullable = false)
    private Boolean hasHistoricalData;

    @Column(name = "historical_data_years")
    private Integer historicalDataYears;

    @Column(name = "has_auto_categorization", nullable = false)
    private Boolean hasAutoCategorization;

    @Column(name = "has_custom_categories", nullable = false)
    private Boolean hasCustomCategories;

    @Column(name = "has_multiple_users", nullable = false)
    private Boolean hasMultipleUsers;

    @Column(name = "has_api_access", nullable = false)
    private Boolean hasApiAccess;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public Integer getMaxAccounts() {
        return maxAccounts;
    }

    public void setMaxAccounts(Integer maxAccounts) {
        this.maxAccounts = maxAccounts;
    }

    public Integer getMaxTransactionsPerMonth() {
        return maxTransactionsPerMonth;
    }

    public void setMaxTransactionsPerMonth(Integer maxTransactionsPerMonth) {
        this.maxTransactionsPerMonth = maxTransactionsPerMonth;
    }

    public Boolean getHasAdvancedReports() {
        return hasAdvancedReports;
    }

    public void setHasAdvancedReports(Boolean hasAdvancedReports) {
        this.hasAdvancedReports = hasAdvancedReports;
    }

    public Boolean getHasExportCapabilities() {
        return hasExportCapabilities;
    }

    public void setHasExportCapabilities(Boolean hasExportCapabilities) {
        this.hasExportCapabilities = hasExportCapabilities;
    }

    public Boolean getHasHistoricalData() {
        return hasHistoricalData;
    }

    public void setHasHistoricalData(Boolean hasHistoricalData) {
        this.hasHistoricalData = hasHistoricalData;
    }

    public Integer getHistoricalDataYears() {
        return historicalDataYears;
    }

    public void setHistoricalDataYears(Integer historicalDataYears) {
        this.historicalDataYears = historicalDataYears;
    }

    public Boolean getHasAutoCategorization() {
        return hasAutoCategorization;
    }

    public void setHasAutoCategorization(Boolean hasAutoCategorization) {
        this.hasAutoCategorization = hasAutoCategorization;
    }

    public Boolean getHasCustomCategories() {
        return hasCustomCategories;
    }

    public void setHasCustomCategories(Boolean hasCustomCategories) {
        this.hasCustomCategories = hasCustomCategories;
    }

    public Boolean getHasMultipleUsers() {
        return hasMultipleUsers;
    }

    public void setHasMultipleUsers(Boolean hasMultipleUsers) {
        this.hasMultipleUsers = hasMultipleUsers;
    }

    public Boolean getHasApiAccess() {
        return hasApiAccess;
    }

    public void setHasApiAccess(Boolean hasApiAccess) {
        this.hasApiAccess = hasApiAccess;
    }
}


