package com.devalFinance.application.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatePasswordStrengthUseCaseTest {

    private ValidatePasswordStrengthUseCase validatePasswordStrengthUseCase;

    @BeforeEach
    void setUp() {
        validatePasswordStrengthUseCase = new ValidatePasswordStrengthUseCase();
    }

    @Test
    void testValidPassword() {
        assertDoesNotThrow(() -> validatePasswordStrengthUseCase.execute("Password123"));
    }

    @Test
    void testPasswordTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordStrengthUseCase.execute("Pass1");
        });
    }

    @Test
    void testPasswordWithoutUppercase() {
        assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordStrengthUseCase.execute("password123");
        });
    }

    @Test
    void testPasswordWithoutLowercase() {
        assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordStrengthUseCase.execute("PASSWORD123");
        });
    }

    @Test
    void testPasswordWithoutNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordStrengthUseCase.execute("Password");
        });
    }

    @Test
    void testNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordStrengthUseCase.execute(null);
        });
    }
}


