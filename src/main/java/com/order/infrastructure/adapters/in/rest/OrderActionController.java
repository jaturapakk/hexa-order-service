package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.in.ShipOrderUseCase;
import com.order.domain.model.OrderId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderActionController {

    private final PayOrderUseCase payOrderUseCase;
    private final ShipOrderUseCase shipOrderUseCase;

    public OrderActionController(PayOrderUseCase payOrderUseCase, ShipOrderUseCase shipOrderUseCase) {
        this.payOrderUseCase = payOrderUseCase;
        this.shipOrderUseCase = shipOrderUseCase;
    }

    @PostMapping("/{id}/pay")
    public void payOrder(@PathVariable UUID id) {
        payOrderUseCase.execute(new OrderId(id));
    }

    @PostMapping("/{id}/ship")
    public void shipOrder(@PathVariable UUID id) {
        shipOrderUseCase.execute(new OrderId(id));
    }
}
