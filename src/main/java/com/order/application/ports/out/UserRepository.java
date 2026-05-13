package com.order.application.ports.out;

import com.order.domain.model.User;
import com.order.domain.model.UserId;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UserId id);
}
