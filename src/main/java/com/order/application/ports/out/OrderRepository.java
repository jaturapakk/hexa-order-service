package com.order.application.ports.out;

import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderId id);
}
