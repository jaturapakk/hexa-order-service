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

class ShipOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private ShipOrderService shipOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldShipOrderSuccessfully() {
        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId, UserId.generate(), List.of());
        order.pay(); // Must be paid to be shipped
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        shipOrderService.execute(orderId);

        verify(orderRepository, times(1)).save(order);
        verify(eventPublisher, times(1)).publishOrderShipped(order);
        verify(eventPublisher, times(1)).publishOrderStatusChanged(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        OrderId orderId = OrderId.generate();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> shipOrderService.execute(orderId));
    }
}
