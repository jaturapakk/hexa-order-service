package com.order.infrastructure.config;

import com.order.application.ports.in.*;
import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.UserRepository;
import com.order.application.services.*;
import com.order.infrastructure.adapters.out.messaging.ConsoleOrderEventPublisher;
import com.order.infrastructure.adapters.out.persistence.PostgresOrderRepository;
import com.order.infrastructure.adapters.out.persistence.PostgresUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderRepository orderRepository(PostgresOrderRepository postgresOrderRepository) {
        return postgresOrderRepository;
    }

    @Bean
    public UserRepository userRepository(PostgresUserRepository postgresUserRepository) {
        return postgresUserRepository;
    }

    @Bean
    public OrderEventPublisher orderEventPublisher() {
        return new ConsoleOrderEventPublisher();
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository repository) {
        return new CreateUserService(repository);
    }

    @Bean
    public PlaceOrderUseCase placeOrderUseCase(OrderRepository orderRepository, 
                                              UserRepository userRepository, 
                                              OrderEventPublisher publisher) {
        return new PlaceOrderService(orderRepository, userRepository, publisher);
    }

    @Bean
    public PayOrderUseCase payOrderUseCase(OrderRepository orderRepository, 
                                          UserRepository userRepository, 
                                          OrderEventPublisher publisher) {
        return new PayOrderService(orderRepository, userRepository, publisher);
    }

    @Bean
    public ShipOrderUseCase shipOrderUseCase(OrderRepository repository, OrderEventPublisher publisher) {
        return new ShipOrderService(repository, publisher);
    }

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderRepository repository, OrderEventPublisher publisher) {
        return new CancelOrderService(repository, publisher);
    }
}
