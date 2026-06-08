package com.order.infrastructure.adapters.out.persistence.repository;

import com.order.infrastructure.adapters.out.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
