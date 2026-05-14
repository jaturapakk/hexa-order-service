package com.order.domain.model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class Order {
    private final OrderId id;
    private final UserId userId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Money totalAmount;
    private final Instant createdAt;

    public Order(OrderId id, UserId userId, List<OrderItem> items) {
        this.id = id;
        this.userId = userId;
        this.items = List.copyOf(items);
        this.status = OrderStatus.PENDING;
        this.totalAmount = calculateTotal(items);
        this.createdAt = Instant.now();
    }

    public Order(OrderId id, UserId userId, List<OrderItem> items, OrderStatus status, Money totalAmount, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = List.copyOf(items);
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    private Money calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::total)
                .reduce(Money.ZERO, Money::add);
    }

    public void pay() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order can only be paid when in PENDING state");
        }
        this.status = OrderStatus.PAID;
    }

    public void ship() {
        if (this.status != OrderStatus.PAID) {
            throw new IllegalStateException("Order can only be shipped when in PAID state");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void cancel() {
        if (this.status == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order cannot be cancelled after shipping");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public OrderId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
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
