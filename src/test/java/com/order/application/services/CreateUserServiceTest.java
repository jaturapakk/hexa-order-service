package com.order.application.services;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class CreateUserServiceTest {

    private UserRepository userRepository;
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        createUserService = new CreateUserService(userRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserUseCase.Command command = new CreateUserUseCase.Command("testUser", new BigDecimal("100.00"));
        
        UserId userId = createUserService.execute(command);
        
        assertNotNull(userId);
        verify(userRepository).save(any(User.class));
    }
}
