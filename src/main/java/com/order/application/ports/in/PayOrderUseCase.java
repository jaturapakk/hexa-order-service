package com.order.application.ports.in;

import com.order.domain.model.OrderId;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;

public interface PayOrderUseCase {
    void execute(Command command);
    record Command(UserId userId, OrderId orderId){}

}
