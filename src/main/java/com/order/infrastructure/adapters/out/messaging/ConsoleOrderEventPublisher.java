package com.order.infrastructure.adapters.out.messaging;

import com.order.application.ports.out.OrderEventPublisher;
import com.order.domain.model.Order;

public class ConsoleOrderEventPublisher implements OrderEventPublisher {
    @Override
    public void publishOrderCreated(Order order) {
        System.out.println("[REALTIME EVENT] Order Created: " + order.getId().value() + 
            " for User: " + order.getUserId().value() + 
            " Total: " + order.getTotalAmount().amount());
    }

    @Override
    public void publishOrderStatusChanged(Order order) {
        System.out.println("[REALTIME EVENT] Order Status Changed: " + order.getId().value() + 
            " New Status: " + order.getStatus());
    }

    @Override
    public void publishOrderShipped(Order order) {
        System.out.println("[REALTIME EVENT] Order Shipped! Notify Warehouse and Logistics: " + order.getId().value());
    }

    @Override
    public void publishOrderCancelled(Order order) {
        System.out.println("[REALTIME EVENT] Order Cancelled. Process refund if necessary: " + order.getId().value());
    }
}
