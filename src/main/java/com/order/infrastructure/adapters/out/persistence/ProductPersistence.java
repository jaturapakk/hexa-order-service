package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.ProductRepository;
import com.order.domain.model.Money;
import com.order.domain.model.Product;
import com.order.domain.model.ProductId;
import com.order.domain.model.UserId;
import com.order.infrastructure.adapters.out.persistence.entities.ProductEntity;
import com.order.infrastructure.adapters.out.persistence.repository.JpaProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductPersistence implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    public ProductPersistence(JpaProductRepository jpaProductRepository){
        this.jpaProductRepository =  jpaProductRepository;
    }
    @Override
    public void save(List<Product> products) {
        List<ProductEntity> productEntities = products.stream().map(
                product -> new ProductEntity(
                        product.getProductId().value(),
                        product.getProductName(),
                        product.getPricePerUnit().amount(),
                        product.getQuantity(),
                        product.getUserId().value())
        ).toList();
        jpaProductRepository.saveAll(productEntities);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return jpaProductRepository.findById(productId.value())
                .map(this::mapToDomain);
    }

    private Product mapToDomain(ProductEntity productEntity){
        return new Product(new ProductId(productEntity.getId()),
                productEntity.getName(),
                productEntity.getQuantity(),
                new Money(productEntity.getPricePerUnit()),
                new UserId(productEntity.getUserId())
        );
    }

}
