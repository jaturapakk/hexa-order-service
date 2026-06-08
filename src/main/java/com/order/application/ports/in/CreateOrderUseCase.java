package com.order.application.ports.in;

import com.order.domain.model.OrderId;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;

import java.util.List;

public interface CreateOrderUseCase {
    OrderId execute(Command command);
    record Command(UserId userId, List<CommandComponent> productItem){}
    record CommandComponent(ProductId productId, Integer integer){}
}
