package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.UserRepository;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class  InMemoryUserRepository implements UserRepository {
    private final Map<UserId, User> database = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        database.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.ofNullable(database.get(id));
    }
}
