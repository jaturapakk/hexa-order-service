package com.order.infrastructure.adapters.out.persistence;

import com.order.domain.model.Event;
import com.order.domain.model.EventType;
import com.order.infrastructure.adapters.out.persistence.entities.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class PostgresEventRepositoryTest {

    @Mock
    private JpaEventRepository jpaEventRepository;

    private PostgresEventRepository postgresEventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postgresEventRepository = new PostgresEventRepository(jpaEventRepository);
    }

    @Test
    void shouldSaveEvent() {
        UUID eventId = UUID.randomUUID();
        UUID aggregateId = UUID.randomUUID();
        Event event = new Event(eventId, aggregateId, 1L, Instant.now(), EventType.ORDER_CREATED, "payload");

        postgresEventRepository.save(event);

        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(jpaEventRepository).save(entityCaptor.capture());

        EventEntity savedEntity = entityCaptor.getValue();
        assertEquals(eventId, savedEntity.getId());
        assertEquals(aggregateId, savedEntity.getAggregateId());
        assertEquals(EventType.ORDER_CREATED, savedEntity.getType());
    }
}
