package com.devalFinance.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateUserWithDefaultValues() {
        User user = new User();
        
        assertNotNull(user);
        assertTrue(user.getActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testUserFullName() {
        User user = new User();
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        
        assertEquals("Juan Pérez", user.getFullName());
    }

    @Test
    void testUserFullNameWithOnlyFirstName() {
        User user = new User();
        user.setFirstName("Juan");
        
        assertEquals("Juan", user.getFullName());
    }

    @Test
    void testUserFullNameWithOnlyLastName() {
        User user = new User();
        user.setLastName("Pérez");
        
        assertEquals("Pérez", user.getFullName());
    }

    @Test
    void testUserFullNameWithNoName() {
        User user = new User();
        user.setEmail("test@example.com");
        
        assertEquals("test@example.com", user.getFullName());
    }

    @Test
    void testUserIsActive() {
        User user = new User();
        assertTrue(user.isActive());
        
        user.setActive(false);
        assertFalse(user.isActive());
    }
}

