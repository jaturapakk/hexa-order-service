package com.order.application.ports.in;

import com.order.domain.model.OrderId;

public interface PayOrderUseCase {
    void execute(OrderId orderId);
}
