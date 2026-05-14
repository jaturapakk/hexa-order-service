package com.order.application.services;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import org.springframework.transaction.annotation.Transactional;


public class CreateUserService implements CreateUserUseCase {
    private final UserRepository userRepository;

    public CreateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserId execute(Command command) {
        UserId userId = UserId.generate();
        User user = new User(
                userId,
                command.name(),
                command.email(),
                command.initialBalance()
        );
        userRepository.save(user);
        return userId;
    }
}
