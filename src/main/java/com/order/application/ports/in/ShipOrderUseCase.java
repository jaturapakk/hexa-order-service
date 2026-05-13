package com.order.application.ports.in;

import com.order.domain.model.OrderId;

public interface ShipOrderUseCase {
    void execute(OrderId orderId);
}
