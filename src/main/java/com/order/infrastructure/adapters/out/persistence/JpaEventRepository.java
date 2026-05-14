package com.order.infrastructure.adapters.out.persistence;

import com.order.infrastructure.adapters.out.persistence.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<EventEntity, UUID> {
}
