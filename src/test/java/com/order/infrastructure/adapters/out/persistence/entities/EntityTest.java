package com.order.infrastructure.adapters.out.persistence.entities;

import com.order.domain.model.OrderStatus;
import com.order.domain.model.EventType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityTest {

    @Test
    void testUserEntity() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("Name");
        entity.setEmail("Email");
        entity.setBalance(BigDecimal.TEN);

        assertEquals(id, entity.getId());
        assertEquals("Name", entity.getName());
        assertEquals("Email", entity.getEmail());
        assertEquals(BigDecimal.TEN, entity.getBalance());
    }

    @Test
    void testOrderEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        List<OrderItemEntity> items = new ArrayList<>();
        
        OrderEntity entity = new OrderEntity();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setStatus(OrderStatus.PENDING);
        entity.setTotalAmount(BigDecimal.TEN);
        entity.setCreatedAt(now);
        entity.setItems(items);

        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUserId());
        assertEquals(OrderStatus.PENDING, entity.getStatus());
        assertEquals(BigDecimal.TEN, entity.getTotalAmount());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(items, entity.getItems());
    }

    @Test
    void testOrderEntityWithNullItems() {
        OrderEntity entity = new OrderEntity(UUID.randomUUID(), UUID.randomUUID(), null, OrderStatus.PENDING, BigDecimal.TEN, Instant.now());
        assertNull(entity.getItems());
    }

    @Test
    void testOrderItemEntity() {
        Long id = 1L;
        UUID productId = UUID.randomUUID();
        OrderEntity order = new OrderEntity();
        
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(id);
        entity.setOrder(order);
        entity.setProductId(productId);
        entity.setQuantity(5);
        entity.setPricePerUnit(BigDecimal.ONE);

        assertEquals(id, entity.getId());
        assertEquals(order, entity.getOrder());
        assertEquals(productId, entity.getProductId());
        assertEquals(5, entity.getQuantity());
        assertEquals(BigDecimal.ONE, entity.getPricePerUnit());
    }

    @Test
    void testEventEntity() {
        UUID id = UUID.randomUUID();
        UUID aggId = UUID.randomUUID();
        Instant now = Instant.now();
        
        EventEntity entity = new EventEntity();
        entity.setId(id);
        entity.setAggregateId(aggId);
        entity.setVersion(1L);
        entity.setTimestamp(now);
        entity.setType(EventType.ORDER_CREATED);
        entity.setPayload("Payload");

        assertEquals(id, entity.getId());
        assertEquals(aggId, entity.getAggregateId());
        assertEquals(1L, entity.getVersion());
        assertEquals(now, entity.getTimestamp());
        assertEquals(EventType.ORDER_CREATED, entity.getType());
        assertEquals("Payload", entity.getPayload());
    }

    @Test
    void testOrderEntityWithItems() {
        UUID id = UUID.randomUUID();
        OrderItemEntity item = new OrderItemEntity();
        List<OrderItemEntity> items = List.of(item);
        
        OrderEntity entity = new OrderEntity(id, UUID.randomUUID(), items, OrderStatus.PENDING, BigDecimal.TEN, Instant.now());
        
        assertEquals(entity, item.getOrder());
    }

    @Test
    void testEventEntityWithVersionZero() {
        EventEntity entity = new EventEntity(UUID.randomUUID(), UUID.randomUUID(), 0L, Instant.now(), EventType.ORDER_CREATED, "payload");
        assertEquals(1L, entity.getVersion());
    }

    @Test
    void testEventEntityWithVersionNonZero() {
        EventEntity entity = new EventEntity(UUID.randomUUID(), UUID.randomUUID(), 5L, Instant.now(), EventType.ORDER_CREATED, "payload");
        assertEquals(6L, entity.getVersion());
    }
}
