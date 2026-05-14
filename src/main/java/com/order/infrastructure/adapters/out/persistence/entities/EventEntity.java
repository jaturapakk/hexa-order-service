package com.order.infrastructure.adapters.out.persistence.entities;

import com.order.domain.model.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.*;

import java.time.Instant;

import java.util.UUID;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private long version;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(nullable = false)
    private String payload;

    public EventEntity() {
    }

    public EventEntity(UUID id, UUID aggregateId, long version, Instant timestamp, EventType type, String payload) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.version = version + 1;
        this.timestamp = timestamp;
        this.type = type;
        this.payload = payload;
        if (version == 0) {
            this.version = 1;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
