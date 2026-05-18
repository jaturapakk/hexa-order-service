package com.order.application.services;

import com.order.application.ports.out.OrderEventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PayOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private PayOrderService payOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPayOrderSuccessfully() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        Order order = new Order(orderId, userId, List.of());
        User user = new User(userId, "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        payOrderService.execute(orderId);

        verify(userRepository, times(1)).save(user);
        verify(orderRepository, times(1)).save(order);
        verify(eventPublisher, times(1)).publishOrderStatusChanged(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        OrderId orderId = OrderId.generate();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> payOrderService.execute(orderId));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        OrderId orderId = OrderId.generate();
        UserId userId = UserId.generate();
        Order order = new Order(orderId, userId, List.of());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> payOrderService.execute(orderId));
    }
}
