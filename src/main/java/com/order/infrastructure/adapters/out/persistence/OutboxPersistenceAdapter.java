package com.order.infrastructure.adapters.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.application.ports.out.EventPublisher;
import com.order.domain.model.DomainEvent;
import com.order.infrastructure.adapters.out.persistence.entities.OutboxEntity;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxRepository;
import org.springframework.stereotype.Component;

@Component
public class OutboxPersistenceAdapter implements EventPublisher {

    private final JpaOutboxRepository jpaOutboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxPersistenceAdapter(JpaOutboxRepository jpaOutboxRepository, ObjectMapper objectMapper) {
        this.jpaOutboxRepository = jpaOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEntity outboxEntity = new OutboxEntity(
                    event.getEventId(),
                    event.getClass().getSimpleName(),
                    payload
            );
            jpaOutboxRepository.save(outboxEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing domain event", e);
        }
    }
}
