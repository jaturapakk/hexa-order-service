package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.in.CreateOrderUseCase;
import com.order.domain.model.OrderId;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PayOrderUseCase payOrderUseCase;
    private final CreateOrderUseCase shipOrderUseCase;

    public OrderController(PayOrderUseCase payOrderUseCase, CreateOrderUseCase shipOrderUseCase){
        this.payOrderUseCase = payOrderUseCase;
        this.shipOrderUseCase = shipOrderUseCase;
    }

    @PostMapping("/pay")
    public void payOrders(@Validated PayOrderRequest request){
        payOrderUseCase.execute(new PayOrderUseCase.Command(new UserId(request.userId()), new OrderId(request.orderId())));
    }

    @PostMapping("/create")
    public CreateOrderResponse createOrder(@Validated CreateOrderRequest request){
        OrderId orderId = shipOrderUseCase.execute(new CreateOrderUseCase.Command(
                new UserId(request.userId()),
                    request.productItems.stream().map(
                            p -> new CreateOrderUseCase.CommandComponent(new ProductId(p.productId), p.quantity)
                    ).toList()
                ));
        return new CreateOrderResponse(orderId);
    }

    public record PayOrderRequest(UUID userId, UUID orderId){}
    public record CreateOrderRequest(UUID userId, List<ProductItem> productItems){}
    public record ProductItem(UUID productId, Integer quantity){}
    public record CreateOrderResponse(OrderId orderId){}
}
