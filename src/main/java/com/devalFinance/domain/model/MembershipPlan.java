package com.devalFinance.domain.model;

import java.util.UUID;

public class MembershipPlan {
    private UUID id;
    private PlanType planType;
    private Integer maxAccounts;
    private Integer maxTransactionsPerMonth;
    private Boolean hasAdvancedReports;
    private Boolean hasExportCapabilities;
    private Boolean hasHistoricalData;
    private Integer historicalDataYears;
    private Boolean hasAutoCategorization;
    private Boolean hasCustomCategories;
    private Boolean hasMultipleUsers;
    private Boolean hasApiAccess;

    public MembershipPlan() {
    }

    public MembershipPlan(UUID id, PlanType planType, Integer maxAccounts, Integer maxTransactionsPerMonth,
                         Boolean hasAdvancedReports, Boolean hasExportCapabilities, Boolean hasHistoricalData,
                         Integer historicalDataYears, Boolean hasAutoCategorization, Boolean hasCustomCategories,
                         Boolean hasMultipleUsers, Boolean hasApiAccess) {
        this.id = id;
        this.planType = planType;
        this.maxAccounts = maxAccounts;
        this.maxTransactionsPerMonth = maxTransactionsPerMonth;
        this.hasAdvancedReports = hasAdvancedReports;
        this.hasExportCapabilities = hasExportCapabilities;
        this.hasHistoricalData = hasHistoricalData;
        this.historicalDataYears = historicalDataYears;
        this.hasAutoCategorization = hasAutoCategorization;
        this.hasCustomCategories = hasCustomCategories;
        this.hasMultipleUsers = hasMultipleUsers;
        this.hasApiAccess = hasApiAccess;
    }

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

    public boolean isUnlimitedAccounts() {
        return maxAccounts == null;
    }

    public boolean isUnlimitedTransactions() {
        return maxTransactionsPerMonth == null;
    }

    public boolean canAccessAdvancedReports() {
        return Boolean.TRUE.equals(hasAdvancedReports);
    }

    public boolean canExportReports() {
        return Boolean.TRUE.equals(hasExportCapabilities);
    }

    public boolean canAccessHistoricalData() {
        return Boolean.TRUE.equals(hasHistoricalData);
    }
}


