package com.devalFinance.application.usecase.user;

import org.springframework.stereotype.Component;

@Component
public class ValidatePasswordStrengthUseCase {
    
    private static final int MIN_LENGTH = 8;
    private static final String PATTERN_UPPERCASE = ".*[A-Z].*";
    private static final String PATTERN_LOWERCASE = ".*[a-z].*";
    private static final String PATTERN_DIGIT = ".*[0-9].*";

    public void execute(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos " + MIN_LENGTH + " caracteres"
            );
        }
        
        if (!password.matches(PATTERN_UPPERCASE)) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        }
        
        if (!password.matches(PATTERN_LOWERCASE)) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula");
        }
        
        if (!password.matches(PATTERN_DIGIT)) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        }
    }
}


