package com.order.infrastructure.adapters.out.persistence.repository;

import com.order.infrastructure.adapters.out.persistence.entities.OutboxEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaOutboxRepository extends JpaRepository<OutboxEntity, UUID> {
    
    @Query(value = "SELECT * FROM outbox ORDER BY created_at ASC FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<OutboxEntity> findNextBatch(Pageable pageable);
}
