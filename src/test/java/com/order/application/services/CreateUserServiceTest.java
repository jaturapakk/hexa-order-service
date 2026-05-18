package com.order.application.services;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Money;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserUseCase.Command command = new CreateUserUseCase.Command(
                "John Doe",
                "john@example.com",
                new Money(new BigDecimal("100.00"))
        );

        UserId userId = createUserService.execute(command);

        assertNotNull(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
