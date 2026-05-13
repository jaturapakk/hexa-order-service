package com.order.domain.model;

public record OrderItem(ProductId productId, int quantity, Money pricePerUnit) {
    public Money total() {
        return pricePerUnit.multiply(quantity);
    }
}
