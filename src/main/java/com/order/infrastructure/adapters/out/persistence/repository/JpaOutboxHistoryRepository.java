package com.order.infrastructure.adapters.out.persistence.repository;

import com.order.infrastructure.adapters.out.persistence.entities.OutboxHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

public interface JpaOutboxHistoryRepository extends JpaRepository<OutboxHistoryEntity, UUID> {
    
    @Transactional
    @Modifying
    @Query("DELETE FROM OutboxHistoryEntity o WHERE o.processedAt < :threshold")
    void deleteOlderThan(Instant threshold);
}
