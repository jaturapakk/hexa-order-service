package com.order.infrastructure.adapters.out.persistence;

import com.order.domain.model.Money;
import com.order.domain.model.User;
import com.order.domain.model.UserId;
import com.order.infrastructure.adapters.out.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PostgresUserRepositoryTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    private PostgresUserRepository postgresUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postgresUserRepository = new PostgresUserRepository(jpaUserRepository);
    }

    @Test
    void shouldSaveUser() {
        UserId userId = UserId.generate();
        User user = new User(userId, "John Doe", "john@example.com", new Money(new BigDecimal("100.00")));

        postgresUserRepository.save(user);

        ArgumentCaptor<UserEntity> entityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(jpaUserRepository).save(entityCaptor.capture());

        UserEntity savedEntity = entityCaptor.getValue();
        assertEquals(userId.value(), savedEntity.getId());
        assertEquals("John Doe", savedEntity.getName());
        assertEquals("john@example.com", savedEntity.getEmail());
        assertEquals(new BigDecimal("100.00"), savedEntity.getBalance());
    }

    @Test
    void shouldFindUserById() {
        UUID uuid = UUID.randomUUID();
        UserEntity entity = new UserEntity(uuid, "Jane Doe", "jane@example.com", new BigDecimal("50.00"));
        when(jpaUserRepository.findById(uuid)).thenReturn(Optional.of(entity));

        Optional<User> userOptional = postgresUserRepository.findById(new UserId(uuid));

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(uuid, user.getId().value());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals(new BigDecimal("50.00"), user.getBalance().amount());
    }
}
