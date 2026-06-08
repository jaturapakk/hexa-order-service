package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.OrderRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;

import java.util.Optional;

public class OrderPersistence implements OrderRepository {
    @Override
    public void save(Order order) {
        OrderEntity
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return Optional.empty();
    }
}
