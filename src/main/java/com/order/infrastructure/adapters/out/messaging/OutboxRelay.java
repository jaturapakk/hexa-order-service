package com.order.infrastructure.adapters.out.messaging;

import com.order.infrastructure.adapters.out.persistence.entities.OutboxEntity;
import com.order.infrastructure.adapters.out.persistence.entities.OutboxFailedEntity;
import com.order.infrastructure.adapters.out.persistence.entities.OutboxHistoryEntity;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxFailedRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxHistoryRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final JpaOutboxRepository outboxRepository;
    private final JpaOutboxHistoryRepository historyRepository;
    private final JpaOutboxFailedRepository failedRepository;
    private final KafkaMessagingAdapter kafkaMessagingAdapter;

    @Value("${outbox.polling.batch-size:10}")
    private int batchSize;

    @Value("${outbox.max-retries:5}")
    private int maxRetries;

    public OutboxRelay(JpaOutboxRepository outboxRepository,
                       JpaOutboxHistoryRepository historyRepository,
                       JpaOutboxFailedRepository failedRepository,
                       KafkaMessagingAdapter kafkaMessagingAdapter) {
        this.outboxRepository = outboxRepository;
        this.historyRepository = historyRepository;
        this.failedRepository = failedRepository;
        this.kafkaMessagingAdapter = kafkaMessagingAdapter;
    }

    @Scheduled(fixedDelayString = "${outbox.polling.interval-ms:5000}")
    @Transactional
    public void relayEvents() {
        List<OutboxEntity> events = outboxRepository.findNextBatch(PageRequest.of(0, batchSize));
        
        if (events.isEmpty()) {
            return;
        }

        log.info("Relaying {} events to Kafka", events.size());

        for (OutboxEntity event : events) {
            try {
                // Topic name could be derived from eventType or a mapping
                String topic = "order-events"; 
                kafkaMessagingAdapter.send(topic, event.getId().toString(), event.getPayload());
                
                // Success: Archive and delete
                historyRepository.save(new OutboxHistoryEntity(event));
                outboxRepository.delete(event);
                
            } catch (Exception e) {
                log.error("Failed to relay event {}: {}", event.getId(), e.getMessage());
                handleFailure(event, e.getMessage());
            }
        }
    }

    private void handleFailure(OutboxEntity event, String errorMessage) {
        event.setRetryCount(event.getRetryCount() + 1);
        if (event.getRetryCount() >= maxRetries) {
            log.warn("Event {} reached max retries. Moving to failed table.", event.getId());
            failedRepository.save(new OutboxFailedEntity(event, errorMessage));
            outboxRepository.delete(event);
        } else {
            outboxRepository.save(event);
        }
    }
}
