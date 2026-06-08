package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.domain.model.UserId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    public UserController(CreateUserUseCase createUserUseCase){
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody @Validated CreateUserRequest request){
        UserId userId = createUserUseCase.execute(new CreateUserUseCase.Command(
                request.userName(), request.initialBalance
        ));
        return new UserResponse(userId.value());
    }

    public record CreateUserRequest(String userName, BigDecimal initialBalance){}
    public record UserResponse(UUID userId){}

}
