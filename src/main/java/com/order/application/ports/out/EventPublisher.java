package com.order.application.ports.out;

import com.order.domain.model.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
