package com.order.application.ports.in;

import com.order.domain.model.Money;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;

import java.util.List;

public interface PlaceProductUseCase {
    List<ProductId> execute(Command command);
    record Command(UserId userId, List<ComponentCommand> products){}
    record ComponentCommand(ProductId productId, String productName, Integer quantity, Money pricePerUnit){}
}
