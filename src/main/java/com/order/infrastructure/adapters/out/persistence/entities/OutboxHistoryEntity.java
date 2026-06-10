package com.order.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_history")
public class OutboxHistoryEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant processedAt;

    public OutboxHistoryEntity() {}

    public OutboxHistoryEntity(OutboxEntity outbox) {
        this.id = outbox.getId();
        this.eventType = outbox.getEventType();
        this.payload = outbox.getPayload();
        this.createdAt = outbox.getCreatedAt();
        this.processedAt = Instant.now();
    }

    // Getters
    public UUID getId() { return id; }
}
