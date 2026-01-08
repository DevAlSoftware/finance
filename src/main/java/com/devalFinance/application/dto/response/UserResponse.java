package com.devalFinance.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private UUID membershipPlanId;
    private LocalDateTime createdAt;
    private Boolean active;

    public UserResponse() {
    }

    public UserResponse(UUID id, String email, String firstName, String lastName, String fullName,
                       UUID membershipPlanId, LocalDateTime createdAt, Boolean active) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.membershipPlanId = membershipPlanId;
        this.createdAt = createdAt;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UUID getMembershipPlanId() {
        return membershipPlanId;
    }

    public void setMembershipPlanId(UUID membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

