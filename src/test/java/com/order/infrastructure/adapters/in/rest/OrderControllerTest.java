package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.PlaceOrderUseCase;
import com.order.domain.model.OrderId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    void shouldPlaceOrder() throws Exception {
        OrderId orderId = OrderId.generate();
        when(placeOrderUseCase.execute(any())).thenReturn(orderId);

        String requestBody = """
                {
                    "userId": "%s",
                    "items": [
                        {
                            "productId": "%s",
                            "quantity": 2,
                            "price": 10.00
                        }
                    ]
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId.value().toString()));
    }
}
