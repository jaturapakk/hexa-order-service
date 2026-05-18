package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
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
        
        order.pay();
        assertEquals(OrderStatus.PAID, order.getStatus());
        
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPayingNonPendingOrder() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        order.pay();
        
        assertThrows(IllegalStateException.class, order::pay);
    }
}
