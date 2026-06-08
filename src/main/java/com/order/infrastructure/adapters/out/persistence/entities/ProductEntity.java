package com.order.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal pricePerUnit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "user_id")
    private UUID userId;

    public ProductEntity(UUID id, String name, BigDecimal pricePerUnit, Integer quantity, UUID userId){
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.userId = userId;
    }

    public UUID getUserId(){
        return userId;
    }
    public UUID getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public BigDecimal getPricePerUnit(){
        return pricePerUnit;
    }
    public Integer getQuantity(){
        return quantity;
    }

}
