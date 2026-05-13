package com.order.application.services;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.User;
import com.order.domain.model.UserId;

public class CreateUserService implements CreateUserUseCase {
    private final UserRepository userRepository;

    public CreateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserId execute(Command command) {
        User user = new User(
                UserId.generate(),
                command.name(),
                command.email(),
                command.initialBalance()
        );
        userRepository.save(user);
        return user.getId();
    }
}
