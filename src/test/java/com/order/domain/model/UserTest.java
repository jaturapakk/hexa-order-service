package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUser() {
        UserId userId = UserId.generate();
        User user = new User(userId, "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));
        
        assertEquals(userId, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(new BigDecimal("100.00"), user.getBalance().amount());
    }

    @Test
    void shouldAddBalance() {
        User user = new User(UserId.generate(), "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));
        user.addBalance(new Money(new BigDecimal("50.00")));
        assertEquals(new BigDecimal("150.00"), user.getBalance().amount());
    }

    @Test
    void shouldDeductBalance() {
        User user = new User(UserId.generate(), "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));
        user.deductBalance(new Money(new BigDecimal("30.00")));
        assertEquals(new BigDecimal("70.00"), user.getBalance().amount());
    }

    @Test
    void shouldThrowExceptionWhenDeductingMoreThanBalance() {
        User user = new User(UserId.generate(), "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));
        assertThrows(IllegalStateException.class, () -> user.deductBalance(new Money(new BigDecimal("150.00"))));
    }
}
