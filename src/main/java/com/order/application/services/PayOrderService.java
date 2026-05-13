package com.order.application.services;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import com.order.domain.model.User;

public class PayOrderService implements PayOrderUseCase {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderEventPublisher eventPublisher;

    public PayOrderService(OrderRepository orderRepository,
                           UserRepository userRepository,
                           OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(OrderId orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId.value()));

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for order: " + orderId.value()));

        user.deductBalance(order.getTotalAmount());
        order.pay();

        userRepository.save(user);
        orderRepository.save(order);
        eventPublisher.publishOrderStatusChanged(order);
    }
}
