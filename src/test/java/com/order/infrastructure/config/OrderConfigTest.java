package com.order.infrastructure.config;

import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.UserRepository;
import com.order.infrastructure.adapters.out.persistence.PostgresOrderRepository;
import com.order.infrastructure.adapters.out.persistence.PostgresUserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class OrderConfigTest {

    private final OrderConfig orderConfig = new OrderConfig();

    @Test
    void shouldCreateBeans() {
        com.order.infrastructure.adapters.out.persistence.JpaOrderRepository jpaOrderRepo = mock(com.order.infrastructure.adapters.out.persistence.JpaOrderRepository.class);
        com.order.infrastructure.adapters.out.persistence.JpaUserRepository jpaUserRepo = mock(com.order.infrastructure.adapters.out.persistence.JpaUserRepository.class);
        
        PostgresOrderRepository orderRepo = new PostgresOrderRepository(jpaOrderRepo);
        PostgresUserRepository userRepo = new PostgresUserRepository(jpaUserRepo);
        OrderEventPublisher publisher = mock(OrderEventPublisher.class);

        assertNotNull(orderConfig.orderRepository(orderRepo));
        assertNotNull(orderConfig.userRepository(userRepo));
        assertNotNull(orderConfig.orderEventPublisher());
        assertNotNull(orderConfig.createUserUseCase(userRepo));
        assertNotNull(orderConfig.placeOrderUseCase(orderRepo, userRepo, publisher));
        assertNotNull(orderConfig.payOrderUseCase(orderRepo, userRepo, publisher));
        assertNotNull(orderConfig.shipOrderUseCase(orderRepo, publisher));
        assertNotNull(orderConfig.cancelOrderUseCase(orderRepo, publisher));
    }
}
