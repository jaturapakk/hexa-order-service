package com.order.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Event(UUID id, UUID aggregateId, long version, Instant timestamp, EventType type, String payload) {
}
