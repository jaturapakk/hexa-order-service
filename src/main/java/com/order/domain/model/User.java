package com.order.domain.model;

public class User {
    private final UserId userId;
    private final String userName;
    private final Money balance;

    public User(UserId userId, String userName, Money balance){
        this.userId = userId;
        this.userName = userName;
        this.balance = balance;
    }

    public UserId getUserId(){
        return userId;
    }

    public String getUserName(){
        return userName;
    }

    public Money getBalance(){
        return balance;
    }

    public void addBalance(Money amount){
        this.balance.add(amount);
    }

    public void deductBalance(Money amount){
        if(!this.balance.isGreaterThanOrEqual(amount)){
            throw new IllegalStateException("Insufficient balance for user");
        }
        this.balance.subtract(amount);
    }
}
