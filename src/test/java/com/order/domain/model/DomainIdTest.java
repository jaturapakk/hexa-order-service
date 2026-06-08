package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DomainIdTest {

    @Test
    void testOrderId() {
        UUID uuid = UUID.randomUUID();
        OrderId id1 = new OrderId(uuid);
        assertEquals(uuid, id1.value());
        
        OrderId id2 = OrderId.generate();
        assertNotNull(id2.value());
        
        OrderId id3 = new OrderId(uuid);
        assertEquals(id1, id3);
        assertEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testProductId() {
        UUID uuid = UUID.randomUUID();
        ProductId id1 = new ProductId(uuid);
        assertEquals(uuid, id1.value());
        
        ProductId id2 = ProductId.generate();
        assertNotNull(id2.value());
        
        ProductId id3 = new ProductId(uuid);
        assertEquals(id1, id3);
    }

    @Test
    void testUserId() {
        UUID uuid = UUID.randomUUID();
        UserId id1 = new UserId(uuid);
        assertEquals(uuid, id1.value());
        
        UserId id2 = UserId.generate();
        assertNotNull(id2.value());
        
        UserId id3 = new UserId(uuid);
        assertEquals(id1, id3);
    }
}
