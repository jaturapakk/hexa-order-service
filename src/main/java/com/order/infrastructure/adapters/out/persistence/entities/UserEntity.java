package com.order.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private BigDecimal balance;

    public UserEntity() {}

    public UserEntity(UUID id, String userName, BigDecimal balance) {
        this.id = id;
        this.userName = userName;
        this.balance = balance;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return userName; }
    public void setName(String name) { this.userName = name; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
