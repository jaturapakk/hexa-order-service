package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.PlaceProductUseCase;
import com.order.domain.model.Money;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final PlaceProductUseCase placeProductUseCase;

    public ProductController(PlaceProductUseCase placeProductUseCase){
        this.placeProductUseCase =  placeProductUseCase;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody @Validated PlaceOrderRequest request){

        List<PlaceProductUseCase.ComponentCommand> products = request.items.stream().map(
                r -> new PlaceProductUseCase.ComponentCommand(ProductId.generate(),r.orderName, r.quantity, new Money(r.price()))
        ).toList();

        List<ProductId> productId = placeProductUseCase.execute(new PlaceProductUseCase.Command(
                new UserId(request.userId),
                products
        ));

        return new OrderResponse(productId.stream().map(ProductId::value).toList());
    }

    public record OrderResponse(List<UUID> productId){}
    public record ItemRequest(String orderName, Integer quantity, BigDecimal price){}
    public record PlaceOrderRequest(UUID userId, List<ItemRequest> items){}

}
