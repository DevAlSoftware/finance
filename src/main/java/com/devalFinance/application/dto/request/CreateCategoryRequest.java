package com.devalFinance.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCategoryRequest {
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;
    
    private String code;
    
    @NotNull(message = "El tipo de transacción es obligatorio")
    private String type; // INCOME o EXPENSE
    
    private String icon;
    
    private String color;

    public CreateCategoryRequest() {
    }

    public CreateCategoryRequest(String name, String code, String type, String icon, String color) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.icon = icon;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

