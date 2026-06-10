package com.order.infrastructure.adapters.out.messaging;

public interface KafkaMessagingAdapter {
    void send(String topic, String key, String payload);
}
