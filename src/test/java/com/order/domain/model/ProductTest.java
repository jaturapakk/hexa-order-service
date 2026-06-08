package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldReduceStock() {
        Product product = new Product(new ProductId(UUID.randomUUID()), "Product A", 10, new Money(new BigDecimal("10.00")), new UserId(UUID.randomUUID()));
        product.reduceStock(3);
        assertEquals(7, product.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        Product product = new Product(new ProductId(UUID.randomUUID()), "Product A", 10, new Money(new BigDecimal("10.00")), new UserId(UUID.randomUUID()));
        assertThrows(IllegalStateException.class, () -> product.reduceStock(11));
    }
}
