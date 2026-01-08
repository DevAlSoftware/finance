package com.devalFinance.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testCreateAccountWithDefaultValues() {
        Account account = new Account();
        
        assertNotNull(account);
        assertEquals(BigDecimal.ZERO, account.getInitialBalance());
        assertEquals(BigDecimal.ZERO, account.getCurrentBalance());
        assertEquals("COP", account.getCurrency());
        assertTrue(account.isActive());
    }

    @Test
    void testUpdateBalance() {
        Account account = new Account();
        account.setCurrentBalance(BigDecimal.valueOf(1000));
        
        account.updateBalance(BigDecimal.valueOf(500));
        
        assertEquals(BigDecimal.valueOf(1500), account.getCurrentBalance());
    }

    @Test
    void testUpdateBalanceWithNegative() {
        Account account = new Account();
        account.setCurrentBalance(BigDecimal.valueOf(1000));
        
        account.updateBalance(BigDecimal.valueOf(-300));
        
        assertEquals(BigDecimal.valueOf(700), account.getCurrentBalance());
    }

    @Test
    void testUpdateBalanceWithNullCurrentBalance() {
        Account account = new Account();
        account.setCurrentBalance(null);
        
        account.updateBalance(BigDecimal.valueOf(100));
        
        assertEquals(BigDecimal.valueOf(100), account.getCurrentBalance());
    }
}

