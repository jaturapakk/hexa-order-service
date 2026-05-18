package com.order.application.services;

import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.domain.model.Order;
import com.order.domain.model.OrderId;
import com.order.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CancelOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private CancelOrderService cancelOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId, UserId.generate(), List.of());
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        cancelOrderService.execute(orderId);

        verify(orderRepository, times(1)).save(order);
        verify(eventPublisher, times(1)).publishOrderCancelled(order);
        verify(eventPublisher, times(1)).publishOrderStatusChanged(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        OrderId orderId = OrderId.generate();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cancelOrderService.execute(orderId));
    }
}
