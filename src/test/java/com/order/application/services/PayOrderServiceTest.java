package com.order.application.services;

import com.order.application.ports.in.PayOrderUseCase;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PayOrderServiceTest {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private PayOrderService payOrderService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        orderRepository = Mockito.mock(OrderRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        payOrderService = new PayOrderService(userRepository, orderRepository, productRepository);
    }

    @Test
    void shouldPayOrderSuccessfully() {
        UserId userId = new UserId(UUID.randomUUID());
        OrderId orderId = new OrderId(UUID.randomUUID());
        ProductId productId = new ProductId(UUID.randomUUID());
        
        User user = new User(userId, "testUser", new Money(new BigDecimal("100.00")));
        OrderItem item = new OrderItem(productId, 2, new Money(new BigDecimal("20.00")));
        Order order = new Order(orderId, userId, List.of(item));
        Product product = new Product(productId, "Product A", 10, new Money(new BigDecimal("20.00")), userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        payOrderService.execute(new PayOrderUseCase.Command(userId, orderId));

        assertEquals(new BigDecimal("60.00"), user.getBalance().amount());
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(8, product.getQuantity());

        verify(userRepository).save(user);
        verify(orderRepository).save(order);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.generate();
        OrderId orderId = OrderId.generate();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> payOrderService.execute(new PayOrderUseCase.Command(userId, orderId)));
        assertEquals("user not found", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UserId userId = UserId.generate();
        OrderId orderId = OrderId.generate();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId, "test", Money.ZERO)));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> payOrderService.execute(new PayOrderUseCase.Command(userId, orderId)));
        assertEquals("order not found", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundDuringPayment() {
        UserId userId = UserId.generate();
        OrderId orderId = OrderId.generate();
        ProductId productId = ProductId.generate();
        
        User user = new User(userId, "test", new Money(new BigDecimal("100.00")));
        OrderItem item = new OrderItem(productId, 1, new Money(new BigDecimal("10.00")));
        Order order = new Order(orderId, userId, List.of(item));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> payOrderService.execute(new PayOrderUseCase.Command(userId, orderId)));
        assertEquals("product not found", ex.getMessage());
    }
}
