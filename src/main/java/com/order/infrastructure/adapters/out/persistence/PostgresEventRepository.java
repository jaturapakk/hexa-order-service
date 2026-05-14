package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.EventRepository;
import com.order.domain.model.Event;
import com.order.infrastructure.adapters.out.persistence.entities.EventEntity;

public class PostgresEventRepository implements EventRepository {
    private final JpaEventRepository jpaEventRepository;

    public PostgresEventRepository(JpaEventRepository jpaEventRepository) {
        this.jpaEventRepository = jpaEventRepository;
    }
    @Override
    public void save(Event event) {
        EventEntity eventEntity = new EventEntity(
                event.id(),
                event.aggregateId(),
                event.version(),
                event.timestamp(),
                event.type(),
                event.payload()
        );
        jpaEventRepository.save(eventEntity);
    }
}
