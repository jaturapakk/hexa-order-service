package com.order.infrastructure.config;

import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.in.PlaceProductUseCase;
import com.order.application.ports.out.ProductRepository;
import com.order.application.ports.out.UserRepository;
import com.order.application.services.CreateUserService;
import com.order.application.services.PlaceProductService;
import com.order.infrastructure.adapters.out.persistence.ProductPersistence;
import com.order.infrastructure.adapters.out.persistence.UserPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public UserRepository userRepository(UserPersistence userPersistence){
        return userPersistence;
    }

    @Bean
    public ProductRepository productRepository(ProductPersistence productPersistence){
        return productPersistence;
    }

    @Bean
    public CreateUserUseCase createUserUseCase(CreateUserService createUserService){
        return createUserService;
    }

    @Bean
    public PlaceProductUseCase placeProductUseCase(PlaceProductService placeProductService){
        return placeProductService;
    }
}
