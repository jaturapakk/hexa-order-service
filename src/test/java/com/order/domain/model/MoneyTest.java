package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldAddMoney() {
        Money m1 = new Money(new BigDecimal("10.00"));
        Money m2 = new Money(new BigDecimal("5.50"));
        Money result = m1.add(m2);
        assertEquals(new BigDecimal("15.50"), result.amount());
    }

    @Test
    void shouldSubtractMoney() {
        Money m1 = new Money(new BigDecimal("10.00"));
        Money m2 = new Money(new BigDecimal("5.50"));
        Money result = m1.subtract(m2);
        assertEquals(new BigDecimal("4.50"), result.amount());
    }

    @Test
    void shouldMultiplyMoney() {
        Money m1 = new Money(new BigDecimal("10.00"));
        Money result = m1.multiply(3);
        assertEquals(new BigDecimal("30.00"), result.amount());
    }

    @Test
    void shouldCheckGreaterThanOrEqual() {
        Money m1 = new Money(new BigDecimal("10.00"));
        Money m2 = new Money(new BigDecimal("5.00"));
        Money m3 = new Money(new BigDecimal("10.00"));
        Money m4 = new Money(new BigDecimal("15.00"));

        assertTrue(m1.isGreaterThanOrEqual(m2));
        assertTrue(m1.isGreaterThanOrEqual(m3));
        assertFalse(m1.isGreaterThanOrEqual(m4));
    }

    @Test
    void shouldHaveZeroConstant() {
        assertEquals(new BigDecimal("0.00"), Money.ZERO.amount());
    }
}
