package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Money;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import com.order.infrastructure.adapters.out.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostgresUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public PostgresUserRepository(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void save(User user) {
        UserEntity entity = new UserEntity(
                user.getId().value(),
                user.getName(),
                user.getEmail(),
                user.getBalance().amount()
        );
        jpaUserRepository.save(entity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id.value())
                .map(entity -> new User(
                        new UserId(entity.getId()),
                        entity.getName(),
                        entity.getEmail(),
                        new Money(entity.getBalance())
                ));
    }
}
