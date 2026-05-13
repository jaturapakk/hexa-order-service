package com.order.application.services;

import com.order.application.ports.in.PlaceOrderUseCase;
import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;

public class PlaceOrderService implements PlaceOrderUseCase {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderEventPublisher eventPublisher;

    public PlaceOrderService(OrderRepository orderRepository, 
                             UserRepository userRepository,
                             OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public OrderId execute(Command command) {
        userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + command.userId().value()));

        Order order = new Order(
                OrderId.generate(),
                command.userId(),
                command.items()
        );

        orderRepository.save(order);
        eventPublisher.publishOrderCreated(order);

        return order.getId();
    }
}
