package com.order.domain.model;

import java.time.Instant;
import java.util.List;

public class Order {
    private final OrderId orderId;
    private final UserId userId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Money totalAmount;
    private final Instant createdAt;

    public Order(OrderId orderId, UserId userId, List<OrderItem> items){
        this(orderId, userId, items, OrderStatus.PENDING, Instant.now());
    }

    public Order(OrderId orderId, UserId userId, List<OrderItem> items, OrderStatus status, Instant createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.totalAmount = calculateAmount(items);
        this.createdAt = createdAt;
    }

    Money calculateAmount(List<OrderItem> items){
        return items.stream()
                .map(OrderItem::totalAmount)
                .reduce(Money.ZERO, Money::add);
    }

    public void pay(){
        if (!this.status.equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Order can only be paid when in PENDING state");
        }
        this.status = OrderStatus.PAID;
    }

    public OrderId getOrderId() {
        return orderId;
    }
    public UserId getUserId(){
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
