package com.order.application.ports.in;

import com.order.domain.model.OrderId;

public interface CancelOrderUseCase {
    void execute(OrderId orderId);
}
