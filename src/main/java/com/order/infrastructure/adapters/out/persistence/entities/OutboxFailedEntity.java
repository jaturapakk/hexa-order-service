package com.order.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_failed")
public class OutboxFailedEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant movedAt;

    @Column(columnDefinition = "TEXT")
    private String lastErrorMessage;

    public OutboxFailedEntity() {}

    public OutboxFailedEntity(OutboxEntity outbox, String errorMessage) {
        this.id = outbox.getId();
        this.eventType = outbox.getEventType();
        this.payload = outbox.getPayload();
        this.createdAt = outbox.getCreatedAt();
        this.movedAt = Instant.now();
        this.lastErrorMessage = errorMessage;
    }

    // Getters
    public UUID getId() { return id; }
}
