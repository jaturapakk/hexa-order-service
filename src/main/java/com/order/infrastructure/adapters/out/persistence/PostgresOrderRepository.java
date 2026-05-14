package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.OrderRepository;
import com.order.domain.model.Money;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import com.order.domain.model.OrderItem;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;
import com.order.infrastructure.adapters.out.persistence.entities.OrderEntity;
import com.order.infrastructure.adapters.out.persistence.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PostgresOrderRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public PostgresOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void save(Order order) {
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(item -> new OrderItemEntity(
                        item.productId().value(),
                        item.quantity(),
                        item.pricePerUnit().amount()
                ))
                .collect(Collectors.toList());

        OrderEntity entity = new OrderEntity(
                order.getId().value(),
                order.getUserId().value(),
                itemEntities,
                order.getStatus(),
                order.getTotalAmount().amount(),
                order.getCreatedAt()
        );
        jpaOrderRepository.save(entity);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpaOrderRepository.findById(id.value())
                .map(this::mapToDomain);
    }

    private Order mapToDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(itemEntity -> new OrderItem(
                        new ProductId(itemEntity.getProductId()),
                        itemEntity.getQuantity(),
                        new Money(itemEntity.getPricePerUnit())
                ))
                .collect(Collectors.toList());

        return new Order(
                new OrderId(entity.getId()),
                new UserId(entity.getUserId()),
                items,
                entity.getStatus(),
                new Money(entity.getTotalAmount()),
                entity.getCreatedAt()
        );
    }
}
