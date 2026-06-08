package com.order.domain.model;

public class Product {
    private final String productName;
    private final UserId userId;
    private final ProductId productId;
    private Integer quantity;
    private final Money pricePerUnit;

    public Product(ProductId productId, String productName, Integer quantity, Money pricePerUnit, UserId userId){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.userId = userId;
    }

    public void reduceStock(Integer quantity){
        if (this.quantity < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + productName);
        }
        this.quantity = this.quantity - quantity;
    }

    public static Product create(ProductId productId, String productName, Integer quantity, Money pricePerUnit, UserId userId) {
        return new Product(productId, productName, quantity, pricePerUnit, userId);
    }

    public String getProductName(){
        return this.productName;
    }
    public ProductId getProductId(){
        return productId;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public Money getPricePerUnit(){
        return pricePerUnit;
    }
    public UserId getUserId(){
        return userId;
    }
}
