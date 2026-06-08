package com.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void testInitialMoney(){
        Money money = new Money(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), money.amount());
    }

    @Test
    void shouldAddMoney(){
        Money money1 = new Money(new BigDecimal("20.00"));
        Money money2 = new Money(new BigDecimal("20.00"));
        Money result = money1.add(money2);
        assertEquals(new BigDecimal("40.00"), result.amount());
    }

    @Test
    void shouldSubtract(){
        Money money1 = new Money(new BigDecimal("20.00"));
        Money money2 = new Money(new BigDecimal("20.00"));
        Money result = money1.subtract(money2);
        assertEquals(new BigDecimal("0.00"), result.amount());
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
