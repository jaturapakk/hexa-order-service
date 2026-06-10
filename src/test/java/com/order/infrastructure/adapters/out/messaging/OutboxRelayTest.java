package com.order.infrastructure.adapters.out.messaging;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.domain.model.*;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxFailedRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxHistoryRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaUserRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOrderRepository;
import com.order.infrastructure.adapters.out.persistence.entities.UserEntity;
import com.order.infrastructure.adapters.out.persistence.entities.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class OutboxRelayTest {

    @Autowired
    private PayOrderUseCase payOrderUseCase;

    @Autowired
    private JpaOutboxRepository outboxRepository;

    @Autowired
    private JpaOutboxHistoryRepository historyRepository;

    @Autowired
    private JpaOutboxFailedRepository failedRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaOrderRepository orderRepository;

    @Autowired
    private OutboxRelay outboxRelay;

    @MockBean
    private KafkaMessagingAdapter kafkaMessagingAdapter;

    @BeforeEach
    void setUp() {
        outboxRepository.deleteAll();
        historyRepository.deleteAll();
        failedRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldRelayEventAndMoveToHistoryOnSuccess() {
        // 1. Prepare data
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        userRepository.save(new UserEntity(userId, "testUser", new BigDecimal("100.00")));
        orderRepository.save(new OrderEntity(orderId, userId, List.of(), OrderStatus.PENDING, new BigDecimal("20.00"), Instant.now()));

        // 2. Execute Payment (Triggers Outbox entry)
        payOrderUseCase.execute(new PayOrderUseCase.Command(new UserId(userId), new OrderId(orderId)));

        assertEquals(1, outboxRepository.count(), "Should have 1 event in outbox");

        // 3. Run Relay
        outboxRelay.relayEvents();

        // 4. Verify
        verify(kafkaMessagingAdapter, times(1)).send(anyString(), anyString(), anyString());
        assertEquals(0, outboxRepository.count(), "Outbox should be empty");
        assertEquals(1, historyRepository.count(), "Event should be in history");
    }

    @Test
    void shouldHandleFailureAndEventuallyMoveToFailedTable() {
        // 1. Prepare data
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        userRepository.save(new UserEntity(userId, "testUser", new BigDecimal("100.00")));
        orderRepository.save(new OrderEntity(orderId, userId, List.of(), OrderStatus.PENDING, new BigDecimal("20.00"), Instant.now()));

        // 2. Execute Payment
        payOrderUseCase.execute(new PayOrderUseCase.Command(new UserId(userId), new OrderId(orderId)));

        // 3. Mock Kafka failure
        doThrow(new RuntimeException("Kafka Down")).when(kafkaMessagingAdapter).send(anyString(), anyString(), anyString());

        // 4. Run Relay multiple times (default max retries is 5)
        for (int i = 0; i < 5; i++) {
            outboxRelay.relayEvents();
        }

        // 5. Verify
        assertEquals(0, outboxRepository.count(), "Outbox should be cleared after max retries");
        assertEquals(1, failedRepository.count(), "Event should be in failed table");
        assertEquals(0, historyRepository.count(), "Event should NOT be in history");
    }
}
