package com.order.application.ports.out;

import com.order.domain.model.Product;
import com.order.domain.model.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(List<Product> product);
    Optional<Product> findById(ProductId productId);
}
