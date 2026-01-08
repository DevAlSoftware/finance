package com.devalFinance.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionLimitTest {

    private TransactionLimit transactionLimit;

    @BeforeEach
    void setUp() {
        transactionLimit = new TransactionLimit();
        transactionLimit.setUserId(UUID.randomUUID());
        transactionLimit.setYear(2024);
        transactionLimit.setMonth(1);
        transactionLimit.setMaxAllowed(50);
        transactionLimit.setTransactionCount(0);
    }

    @Test
    void testHasReachedLimitWhenCountEqualsMax() {
        transactionLimit.setTransactionCount(50);
        
        assertTrue(transactionLimit.hasReachedLimit());
    }

    @Test
    void testHasReachedLimitWhenCountExceedsMax() {
        transactionLimit.setTransactionCount(51);
        
        assertTrue(transactionLimit.hasReachedLimit());
    }

    @Test
    void testHasNotReachedLimit() {
        transactionLimit.setTransactionCount(30);
        
        assertFalse(transactionLimit.hasReachedLimit());
    }

    @Test
    void testGetRemainingTransactions() {
        transactionLimit.setTransactionCount(30);
        
        assertEquals(20, transactionLimit.getRemainingTransactions());
    }

    @Test
    void testGetRemainingTransactionsWhenAtLimit() {
        transactionLimit.setTransactionCount(50);
        
        assertEquals(0, transactionLimit.getRemainingTransactions());
    }

    @Test
    void testIncrementCount() {
        int initialCount = transactionLimit.getTransactionCount();
        
        transactionLimit.incrementCount();
        
        assertEquals(initialCount + 1, transactionLimit.getTransactionCount());
    }

    @Test
    void testGetRemainingTransactionsWithNullMaxAllowed() {
        transactionLimit.setMaxAllowed(null);
        
        assertEquals(Integer.MAX_VALUE, transactionLimit.getRemainingTransactions());
    }
}

