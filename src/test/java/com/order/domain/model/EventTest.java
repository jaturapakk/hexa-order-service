package com.order.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void shouldCreateEvent() {
        UUID eventId = UUID.randomUUID();
        UUID aggregateId = UUID.randomUUID();
        Instant now = Instant.now();
        Event event = new Event(eventId, aggregateId, 1L, now, EventType.ORDER_CREATED, "Order created");
        
        assertEquals(eventId, event.id());
        assertEquals(aggregateId, event.aggregateId());
        assertEquals(1L, event.version());
        assertEquals(now, event.timestamp());
        assertEquals(EventType.ORDER_CREATED, event.type());
        assertEquals("Order created", event.payload());
    }

    @Test
    void testEventTypeValues() {
        assertNotNull(EventType.valueOf("USER_REGISTER"));
        assertNotNull(EventType.valueOf("ORDER_CREATED"));
        assertNotNull(EventType.valueOf("ORDER_CANCELLED"));
        assertNotNull(EventType.valueOf("ORDER_UPDATED"));
    }
}
