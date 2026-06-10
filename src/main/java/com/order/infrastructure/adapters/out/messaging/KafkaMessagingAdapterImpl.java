package com.order.infrastructure.adapters.out.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessagingAdapterImpl implements KafkaMessagingAdapter {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessagingAdapterImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topic, String key, String payload) {
        kafkaTemplate.send(topic, key, payload);
    }
}
