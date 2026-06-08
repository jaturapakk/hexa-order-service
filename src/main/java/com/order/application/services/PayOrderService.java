package com.order.application.services;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.ProductRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Order;
import com.order.domain.model.Product;
import com.order.domain.model.User;

public class PayOrderService implements PayOrderUseCase {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public PayOrderService(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(Command command) {
        User user = userRepository.findById(command.userId()).orElseThrow(() -> new IllegalStateException("user not found"));
        Order order = orderRepository.findById(command.orderId()).orElseThrow(() -> new IllegalStateException("order not found"));
        
        user.deductBalance(order.getTotalAmount());
        order.pay();
        
        order.getItems().forEach(item -> {
            Product product = productRepository.findById(item.productId()).orElseThrow(() -> new IllegalStateException("product not found"));
            product.reduceStock(item.quantity());
            productRepository.save(product);
        });

        userRepository.save(user);
        orderRepository.save(order);
    }
}
