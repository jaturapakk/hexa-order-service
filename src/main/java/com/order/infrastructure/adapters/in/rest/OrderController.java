package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.PlaceOrderUseCase;
import com.order.domain.model.Money;
import com.order.domain.model.OrderId;
import com.order.domain.model.OrderItem;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        List<OrderItem> items = request.items().stream()
                .map(item -> new OrderItem(
                        new ProductId(item.productId()),
                        item.quantity(),
                        new Money(item.price())
                ))
                .collect(Collectors.toList());

        OrderId orderId = placeOrderUseCase.execute(new PlaceOrderUseCase.Command(
                new UserId(request.userId()),
                items
        ));

        return new OrderResponse(orderId.value());
    }

    public record OrderRequest(UUID userId, List<ItemRequest> items) {}
    public record ItemRequest(UUID productId, int quantity, BigDecimal price) {}
    public record OrderResponse(UUID orderId) {}
}
