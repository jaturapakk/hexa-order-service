package com.order.application.services;

import com.order.application.ports.in.PlaceProductUseCase;
import com.order.application.ports.out.ProductRepository;
import com.order.application.ports.out.UserRepository;
import com.order.domain.model.Product;
import com.order.domain.model.ProductId;
import com.order.domain.model.User;

import java.util.List;

public class PlaceProductService implements PlaceProductUseCase {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public PlaceProductService(ProductRepository productRepository, UserRepository userRepository){
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<ProductId> execute(Command command) {
        User user = userRepository.findById(command.userId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Product> productList = command.products().stream().map(
                l -> Product.create(l.productId(), l.productName(), l.quantity(), l.pricePerUnit(), user.getUserId())
        ).toList();
        productRepository.save(productList);
        return productList.stream().map(Product::getProductId).toList();
    }
}
