package com.order.application.services;

import com.order.application.ports.in.PlaceOrderUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaceOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private PlaceOrderService placeOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        UserId userId = UserId.generate();
        User user = new User(userId, "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ProductId productId = ProductId.generate();
        OrderItem item = new OrderItem(productId, 2, new Money(new BigDecimal("10.00")));
        PlaceOrderUseCase.Command command = new PlaceOrderUseCase.Command(userId, List.of(item));

        OrderId orderId = placeOrderService.execute(command);

        assertNotNull(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(eventPublisher, times(1)).publishOrderCreated(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.generate();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        PlaceOrderUseCase.Command command = new PlaceOrderUseCase.Command(userId, List.of());

        assertThrows(IllegalArgumentException.class, () -> placeOrderService.execute(command));
        verify(orderRepository, never()).save(any(Order.class));
        verify(eventPublisher, never()).publishOrderCreated(any(Order.class));
    }
}
