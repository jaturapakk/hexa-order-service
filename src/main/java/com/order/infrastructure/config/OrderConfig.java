package com.order.infrastructure.config;

import com.order.application.ports.in.CreateOrderUseCase;
import com.order.application.ports.in.CreateUserUseCase;
import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.in.PlaceProductUseCase;
import com.order.application.ports.out.EventPublisher;
import com.order.application.ports.out.OrderRepository;
import com.order.application.ports.out.ProductRepository;
import com.order.application.ports.out.UserRepository;
import com.order.application.services.CreateOrderService;
import com.order.application.services.CreateUserService;
import com.order.application.services.PayOrderService;
import com.order.application.services.PlaceProductService;
import com.order.infrastructure.adapters.out.persistence.OrderPersistence;
import com.order.infrastructure.adapters.out.persistence.ProductPersistence;
import com.order.infrastructure.adapters.out.persistence.UserPersistence;
import com.order.infrastructure.adapters.out.persistence.repository.JpaOrderRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaProductRepository;
import com.order.infrastructure.adapters.out.persistence.repository.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public UserRepository userRepository(JpaUserRepository jpaUserRepository){
        return new UserPersistence(jpaUserRepository);
    }

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository){
        return new ProductPersistence(jpaProductRepository);
    }

    @Bean
    public OrderRepository orderRepository(JpaOrderRepository jpaOrderRepository) {
        return new OrderPersistence(jpaOrderRepository);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository){
        return new CreateUserService(userRepository);
    }

    @Bean
    public PlaceProductUseCase placeProductUseCase(ProductRepository productRepository, UserRepository userRepository){
        return new PlaceProductService(productRepository, userRepository);
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        return new CreateOrderService(userRepository, productRepository, orderRepository);
    }

    @Bean
    public PayOrderUseCase payOrderUseCase(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository, EventPublisher eventPublisher) {
        return new PayOrderService(userRepository, orderRepository, productRepository, eventPublisher);
    }
}
