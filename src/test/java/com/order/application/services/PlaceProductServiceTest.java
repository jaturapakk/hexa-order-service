package com.order.application.services;

import com.order.application.ports.in.PlaceProductUseCase;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlaceProductServiceTest {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private PlaceProductService placeProductService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        placeProductService = new PlaceProductService(productRepository, userRepository);
    }

    @Test
    void shouldPlaceProductsSuccessfully() {
        UserId userId = new UserId(UUID.randomUUID());
        User user = new User(userId, "testUser", new Money(new BigDecimal("100.00")));
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        PlaceProductUseCase.Command command = new PlaceProductUseCase.Command(
                userId,
                List.of(new PlaceProductUseCase.ComponentCommand(ProductId.generate(), "Product A", 10, new Money(new BigDecimal("10.00"))))
        );
        
        List<ProductId> productIds = placeProductService.execute(command);
        
        assertEquals(1, productIds.size());
        verify(productRepository).save(anyList());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.generate();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        PlaceProductUseCase.Command command = new PlaceProductUseCase.Command(userId, List.of());
        
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> placeProductService.execute(command));
        assertEquals("User not found", ex.getMessage());
    }
}
