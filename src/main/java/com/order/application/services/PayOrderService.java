package com.order.application.services;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.User;

public class PayOrderService implements PayOrderUseCase {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public PayOrderService(UserRepository userRepository, OrderRepository orderRepository){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }
    @Override
    public void execute(Command command) {
//        User user =
    }
}
