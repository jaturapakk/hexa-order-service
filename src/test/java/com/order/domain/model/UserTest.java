package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldAddBalance() {
        User user = new User(new UserId(UUID.randomUUID()), "testUser", new Money(new BigDecimal("100.00")));
        user.addBalance(new Money(new BigDecimal("50.00")));
        assertEquals(new BigDecimal("150.00"), user.getBalance().amount());
    }

    @Test
    void shouldDeductBalance() {
        User user = new User(new UserId(UUID.randomUUID()), "testUser", new Money(new BigDecimal("100.00")));
        user.deductBalance(new Money(new BigDecimal("40.00")));
        assertEquals(new BigDecimal("60.00"), user.getBalance().amount());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        User user = new User(new UserId(UUID.randomUUID()), "testUser", new Money(new BigDecimal("100.00")));
        assertThrows(IllegalStateException.class, () -> user.deductBalance(new Money(new BigDecimal("110.00"))));
    }
}
