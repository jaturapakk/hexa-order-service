package com.order.application.ports.in;

import com.order.domain.model.OrderId;
import com.order.domain.model.OrderItem;
import com.order.domain.model.UserId;
import java.util.List;

public interface PlaceOrderUseCase {
    OrderId execute(Command command);

    record Command(UserId userId, List<OrderItem> items) {}
}
