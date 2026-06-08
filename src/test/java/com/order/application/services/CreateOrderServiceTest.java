package com.order.application.services;

import com.order.application.ports.in.CreateOrderUseCase;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.ProductRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateOrderServiceTest {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CreateOrderService createOrderService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        createOrderService = new CreateOrderService(userRepository, productRepository, orderRepository);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        UserId userId = new UserId(UUID.randomUUID());
        ProductId productId = new ProductId(UUID.randomUUID());
        User user = new User(userId, "testUser", new Money(new BigDecimal("100.00")));
        Product product = new Product(productId, "Product A", 10, new Money(new BigDecimal("20.00")), userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        CreateOrderUseCase.Command command = new CreateOrderUseCase.Command(
                userId,
                List.of(new CreateOrderUseCase.CommandComponent(productId, 2))
        );

        OrderId orderId = createOrderService.execute(command);

        assertNotNull(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.generate();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CreateOrderUseCase.Command command = new CreateOrderUseCase.Command(userId, List.of());
        
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> createOrderService.execute(command));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UserId userId = UserId.generate();
        ProductId productId = ProductId.generate();
        User user = new User(userId, "test", Money.ZERO);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        CreateOrderUseCase.Command command = new CreateOrderUseCase.Command(
                userId, 
                List.of(new CreateOrderUseCase.CommandComponent(productId, 1))
        );
        
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> createOrderService.execute(command));
        assertEquals("product not found", ex.getMessage());
    }
}
