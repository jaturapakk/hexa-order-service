package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderInPendingStatus() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        ProductId productId = new ProductId(java.util.UUID.randomUUID());
        OrderItem item = new OrderItem(productId, 2, new Money(new BigDecimal("10.00")));
        
        Order order = new Order(orderId, userId, List.of(item));

        assertEquals(orderId, order.getId());
        assertEquals(userId, order.getUserId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(new BigDecimal("20.00"), order.getTotalAmount().amount());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void shouldTransitionStatusCorrectly() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        
        order.pay();
        assertEquals(OrderStatus.PAID, order.getStatus());
        
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void shouldCancelPendingOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldCancelPaidOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.pay();
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPayingNonPendingOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.pay();
        assertThrows(IllegalStateException.class, order::pay);
        
        order.ship();
        assertThrows(IllegalStateException.class, order::pay);
    }

    @Test
    void shouldThrowExceptionWhenShippingNonPaidOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        assertThrows(IllegalStateException.class, order::ship);
        
        order.cancel();
        assertThrows(IllegalStateException.class, order::ship);
    }

    @Test
    void shouldThrowExceptionWhenCancellingShippedOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.pay();
        order.ship();
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void shouldCreateOrderWithAllFields() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        List<OrderItem> items = List.of();
        OrderStatus status = OrderStatus.PAID;
        Money total = new Money(new BigDecimal("100.00"));
        java.time.Instant now = java.time.Instant.now();
        
        Order order = new Order(orderId, userId, items, status, total, now);
        
        assertEquals(orderId, order.getId());
        assertEquals(userId, order.getUserId());
        assertEquals(items, order.getItems());
        assertEquals(status, order.getStatus());
        assertEquals(total, order.getTotalAmount());
        assertEquals(now, order.getCreatedAt());
    }
}
