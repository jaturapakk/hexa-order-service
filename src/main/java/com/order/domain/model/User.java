package com.order.domain.model;

public class User {
    private final UserId id;
    private final String name;
    private final String email;
    private Money balance;

    public User(UserId id, String name, String email, Money initialBalance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = initialBalance;
    }

    public void deductBalance(Money amount) {
        if (!balance.isGreaterThanOrEqual(amount)) {
            throw new IllegalStateException("Insufficient balance for user: " + id.value());
        }
        this.balance = this.balance.subtract(amount);
    }

    public void addBalance(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public UserId getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Money getBalance() { return balance; }
}
