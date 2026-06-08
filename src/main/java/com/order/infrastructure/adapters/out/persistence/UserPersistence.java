package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Money;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import com.order.infrastructure.adapters.out.persistence.entities.UserEntity;
import com.order.infrastructure.adapters.out.persistence.repository.JpaUserRepository;

import java.util.Optional;

public class UserPersistence implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    public UserPersistence(JpaUserRepository jpaUserRepository){
        this.jpaUserRepository = jpaUserRepository;
    }
    @Override
    public void save(User user) {
        UserEntity userEntity =  new UserEntity(user.getUserId().value(), user.getUserName(), user.getBalance().amount());
        jpaUserRepository.save(userEntity);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaUserRepository.findById(userId.value()).map(
                entity -> new User(
                                new UserId(entity.getId()),
                                entity.getName(),
                                new Money(entity.getBalance()))
        );
    }
}
