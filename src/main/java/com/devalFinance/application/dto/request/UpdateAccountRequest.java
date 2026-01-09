package com.devalFinance.application.dto.request;

import jakarta.validation.constraints.Size;

public class UpdateAccountRequest {
    
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String name;

    public UpdateAccountRequest() {
    }

    public UpdateAccountRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


