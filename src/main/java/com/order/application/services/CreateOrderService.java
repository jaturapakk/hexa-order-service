package com.order.application.services;

import com.order.application.ports.in.CreateOrderUseCase;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.ProductRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import com.order.domain.model.OrderItem;
import com.order.domain.model.Product;
import com.order.domain.model.User;

import java.util.List;

public class CreateOrderService implements CreateOrderUseCase {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CreateOrderService(UserRepository userRepository,
                              ProductRepository productRepository,
                              OrderRepository orderRepository){
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    @Override
    public OrderId execute(Command command) {
        User user = userRepository.findById(command.userId()).orElseThrow(() -> new IllegalStateException("User not found"));
        List<OrderItem> orderItems = command.productItem().stream().map(
                p -> {
                    Product product = productRepository.findById(p.productId()).orElseThrow(() -> new IllegalStateException("product not found"));
                    return new OrderItem(product.getProductId(), p.quantity(), product.getPricePerUnit());
                }
        ).toList();
        Order order = new Order(OrderId.generate(), user.getUserId(), orderItems);
        orderRepository.save(order);
        return order.getOrderId();

    }
}
