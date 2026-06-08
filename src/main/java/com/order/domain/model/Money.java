package com.order.domain.model;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {
    public static final Money ZERO = new Money(new BigDecimal("0.00"));

    public Money add(Money other){
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other){
        return new Money((this.amount.subtract(other.amount)));
    }

    public Money multiply(int factor){
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public boolean isGreaterThanOrEqual(Money other){
        return this.amount.compareTo(other.amount) >= 0;
    }

}
