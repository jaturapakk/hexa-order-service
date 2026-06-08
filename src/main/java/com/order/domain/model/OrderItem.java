package com.order.domain.model;

public record OrderItem(ProductId productId, Integer quantity, Money pricePerUnit) {
    public Money totalAmount(){
        return pricePerUnit.multiply(quantity);
    }
}
