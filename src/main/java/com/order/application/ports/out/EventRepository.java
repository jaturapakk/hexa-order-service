package com.order.application.ports.out;

import com.order.domain.model.Event;

public interface EventRepository {
    void save(Event event);
}
