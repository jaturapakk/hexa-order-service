package com.order.infrastructure.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.application.ports.in.CreateOrderUseCase;
import com.order.application.ports.in.PayOrderUseCase;
import com.order.domain.model.OrderId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PayOrderUseCase payOrderUseCase;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrder() throws Exception {
        OrderId orderId = OrderId.generate();
        OrderController.CreateOrderRequest request = new OrderController.CreateOrderRequest(
                UUID.randomUUID(),
                List.of(new OrderController.ProductItem(UUID.randomUUID(), 2))
        );

        when(createOrderUseCase.execute(any())).thenReturn(orderId);

        mockMvc.perform(post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId.value").value(orderId.value().toString()));
    }

    @Test
    void shouldPayOrder() throws Exception {
        OrderController.PayOrderRequest request = new OrderController.PayOrderRequest(UUID.randomUUID(), UUID.randomUUID());

        doNothing().when(payOrderUseCase).execute(any());

        mockMvc.perform(post("/orders/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
