package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.OrderRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import com.order.infrastructure.adapters.out.persistence.entities.OrderEntity;
import com.order.infrastructure.adapters.out.persistence.entities.OrderItemEntity;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOrderRepository;

import java.util.Optional;

public class OrderPersistence implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public OrderPersistence(JpaOrderRepository jpaOrderRepository){
        this.jpaOrderRepository = jpaOrderRepository;
    }
    @Override
    public void save(Order order) {
        OrderEntity orderEntity = new OrderEntity(
                order.getOrderId().value(),
                order.getUserId().value(),
                order.getItems().stream().map(
                        item -> new OrderItemEntity(
                                item.productId().value(),
                                item.quantity(),
                                item.pricePerUnit().amount()
                        )
                ).toList(),
                order.getStatus(),
                order.getTotalAmount().amount(),
                order.getCreatedAt()
                );

        jpaOrderRepository.save(orderEntity);

    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return Optional.empty();
    }
}
