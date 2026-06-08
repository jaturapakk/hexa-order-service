package com.order.infrastructure.adapters.out.persistence;

import com.order.application.ports.out.ProductRepository;
import com.order.domain.model.Product;
import com.order.infrastructure.adapters.out.persistence.entities.ProductEntity;
import com.order.infrastructure.adapters.out.persistence.repository.JpaProductRepository;

import java.util.List;

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
}
