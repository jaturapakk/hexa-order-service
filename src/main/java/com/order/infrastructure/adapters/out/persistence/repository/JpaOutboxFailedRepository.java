package com.order.infrastructure.adapters.out.persistence.repository;

import com.order.infrastructure.adapters.out.persistence.entities.OutboxFailedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaOutboxFailedRepository extends JpaRepository<OutboxFailedEntity, UUID> {
}
