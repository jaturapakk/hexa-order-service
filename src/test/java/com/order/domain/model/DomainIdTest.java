package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DomainIdTest {

    @Test
    void testOrderId() {
        OrderId id = OrderId.generate();
        assertNotNull(id.value());
        
        UUID uuid = UUID.randomUUID();
        OrderId fromStr = OrderId.fromString(uuid.toString());
        assertEquals(uuid, fromStr.value());
    }

    @Test
    void testUserId() {
        UserId id = UserId.generate();
        assertNotNull(id.value());
        
        UUID uuid = UUID.randomUUID();
        UserId fromStr = UserId.fromString(uuid.toString());
        assertEquals(uuid, fromStr.value());
    }

    @Test
    void testProductId() {
        ProductId id = ProductId.generate();
        assertNotNull(id.value());
        
        UUID uuid = UUID.randomUUID();
        ProductId fromStr = ProductId.fromString(uuid.toString());
        assertEquals(uuid, fromStr.value());
    }
}
