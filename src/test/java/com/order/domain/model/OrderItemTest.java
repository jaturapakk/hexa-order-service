package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void shouldCalculateTotal() {
        ProductId productId = ProductId.generate();
        Money price = new Money(new BigDecimal("10.50"));
        OrderItem item = new OrderItem(productId, 3, price);
        
        assertEquals(productId, item.productId());
        assertEquals(3, item.quantity());
        assertEquals(price, item.pricePerUnit());
        assertEquals(new BigDecimal("31.50"), item.total().amount());
    }
}
