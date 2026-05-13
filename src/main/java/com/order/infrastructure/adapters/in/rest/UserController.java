package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Money;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserRepository userRepository;

    public UserController(CreateUserUseCase createUserUseCase, UserRepository userRepository) {
        this.createUserUseCase = createUserUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        UserId userId = createUserUseCase.execute(new CreateUserUseCase.Command(
                request.name(),
                request.email(),
                new Money(request.initialBalance())
        ));

        return new UserResponse(userId.value());
    }

    @GetMapping("/{id}")
    public UserDetailResponse getUser(@PathVariable UUID id) {
        User user = userRepository.findById(new UserId(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        
        return new UserDetailResponse(user.getId().value(), user.getName(), user.getEmail(), user.getBalance().amount());
    }

    public record UserRequest(String name, String email, BigDecimal initialBalance) {}
    public record UserResponse(UUID userId) {}
    public record UserDetailResponse(UUID userId, String name, String email, BigDecimal balance) {}
}
