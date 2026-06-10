package com.order.infrastructure.adapters.out.messaging;

import com.order.infrastructure.adapters.out.persistence.repository.JpaOutboxHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OutboxPurgeJob {

    private static final Logger log = LoggerFactory.getLogger(OutboxPurgeJob.class);

    private final JpaOutboxHistoryRepository historyRepository;

    @Value("${outbox.history.retention-days:30}")
    private int retentionDays;

    public OutboxPurgeJob(JpaOutboxHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Every day at midnight
    public void purgeOldHistory() {
        log.info("Starting outbox history purge (retention: {} days)", retentionDays);
        Instant threshold = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        historyRepository.deleteOlderThan(threshold);
        log.info("Outbox history purge completed.");
    }
}
