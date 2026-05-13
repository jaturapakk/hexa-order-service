package com.order.application.ports.out;

import com.order.domain.model.Order;

public interface OrderEventPublisher {
    void publishOrderCreated(Order order);
    void publishOrderStatusChanged(Order order);
    void publishOrderShipped(Order order);
    void publishOrderCancelled(Order order);
}
