package com.order.infrastructure.adapters.out.persistence;

import com.order.domain.model.*;
import com.order.infrastructure.adapters.out.persistence.entities.OrderEntity;
import com.order.infrastructure.adapters.out.persistence.entities.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PostgresOrderRepositoryTest {

    @Mock
    private JpaOrderRepository jpaOrderRepository;

    private PostgresOrderRepository postgresOrderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postgresOrderRepository = new PostgresOrderRepository(jpaOrderRepository);
    }

    @Test
    void shouldSaveOrder() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        ProductId productId = ProductId.generate();
        OrderItem item = new OrderItem(productId, 2, new Money(new BigDecimal("10.00")));
        Order order = new Order(orderId, userId, List.of(item));

        postgresOrderRepository.save(order);

        ArgumentCaptor<OrderEntity> entityCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(jpaOrderRepository).save(entityCaptor.capture());

        OrderEntity savedEntity = entityCaptor.getValue();
        assertEquals(orderId.value(), savedEntity.getId());
        assertEquals(userId.value(), savedEntity.getUserId());
        assertEquals(1, savedEntity.getItems().size());
        assertEquals(productId.value(), savedEntity.getItems().get(0).getProductId());
    }

    @Test
    void shouldFindOrderById() {
        UUID orderUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        UUID productUuid = UUID.randomUUID();
        OrderItemEntity itemEntity = new OrderItemEntity(productUuid, 2, new BigDecimal("10.00"));
        OrderEntity entity = new OrderEntity(orderUuid, userUuid, List.of(itemEntity), OrderStatus.PENDING, new BigDecimal("20.00"), Instant.now());
        
        when(jpaOrderRepository.findById(orderUuid)).thenReturn(Optional.of(entity));

        Optional<Order> orderOptional = postgresOrderRepository.findById(new OrderId(orderUuid));

        assertTrue(orderOptional.isPresent());
        Order order = orderOptional.get();
        assertEquals(orderUuid, order.getId().value());
        assertEquals(userUuid, order.getUserId().value());
        assertEquals(1, order.getItems().size());
        assertEquals(productUuid, order.getItems().get(0).productId().value());
    }
}
