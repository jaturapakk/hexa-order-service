package com.order.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaidEvent(
        UUID eventId,
        UUID orderId,
        UUID userId,
        BigDecimal amount,
        Instant occurredAt
) implements DomainEvent {
    public OrderPaidEvent(UUID orderId, UUID userId, BigDecimal amount) {
        this(UUID.randomUUID(), orderId, userId, amount, Instant.now());
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }
}
