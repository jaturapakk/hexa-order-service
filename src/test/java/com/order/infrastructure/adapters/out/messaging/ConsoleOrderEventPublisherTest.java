package com.order.infrastructure.adapters.out.messaging;

import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import com.order.domain.model.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ConsoleOrderEventPublisherTest {

    private final ConsoleOrderEventPublisher publisher = new ConsoleOrderEventPublisher();

    @Test
    void shouldPublishEventsWithoutError() {
        Order order = new Order(OrderId.generate(), UserId.generate(), List.of());
        
        assertDoesNotThrow(() -> publisher.publishOrderCreated(order));
        assertDoesNotThrow(() -> publisher.publishOrderStatusChanged(order));
        assertDoesNotThrow(() -> publisher.publishOrderShipped(order));
        assertDoesNotThrow(() -> publisher.publishOrderCancelled(order));
    }
}
