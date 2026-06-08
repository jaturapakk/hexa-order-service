package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderAndCalculateAmount() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        ProductId p1 = ProductId.generate();
        ProductId p2 = ProductId.generate();
        
        OrderItem item1 = new OrderItem(p1, 2, new Money(new BigDecimal("10.00")));
        OrderItem item2 = new OrderItem(p2, 1, new Money(new BigDecimal("5.00")));
        
        Order order = new Order(orderId, userId, List.of(item1, item2));
        
        assertEquals(orderId, order.getOrderId());
        assertEquals(userId, order.getUserId());
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("25.00"), order.getTotalAmount().amount());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void shouldPayOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.pay();
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPayingNonPendingOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.pay(); // Now PAID
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, order::pay);
        assertEquals("Order can only be paid when in PENDING state", exception.getMessage());
    }

    @Test
    void shouldRebuildOrder() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        Instant now = Instant.now();
        Order order = new Order(orderId, userId, List.of(), OrderStatus.PAID, now);
        
        assertEquals(orderId, order.getOrderId());
        assertEquals(userId, order.getUserId());
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(now, order.getCreatedAt());
    }
}
