package com.order.application.services;

import com.order.application.ports.in.CancelOrderUseCase;
import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;

public class CancelOrderService implements CancelOrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public CancelOrderService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(OrderId orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId.value()));

        order.cancel();
        orderRepository.save(order);
        eventPublisher.publishOrderCancelled(order);
        eventPublisher.publishOrderStatusChanged(order);
    }
}
