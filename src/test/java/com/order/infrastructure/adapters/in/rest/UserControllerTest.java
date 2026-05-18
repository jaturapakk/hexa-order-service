package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Money;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldCreateUser() throws Exception {
        UserId userId = UserId.generate();
        when(createUserUseCase.execute(any())).thenReturn(userId);

        String requestBody = """
                {
                    "name": "John Doe",
                    "email": "john@example.com",
                    "initialBalance": 100.00
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.value().toString()));
    }

    @Test
    void shouldGetUser() throws Exception {
        UserId userId = UserId.generate();
        User user = new User(userId, "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/" + userId.value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.value().toString()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(new UserId(id))).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isBadRequest()); // GlobalExceptionHandler handles this
    }
}
