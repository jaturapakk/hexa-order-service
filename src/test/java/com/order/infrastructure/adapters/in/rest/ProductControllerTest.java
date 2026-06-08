package com.order.infrastructure.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.application.ports.in.PlaceProductUseCase;
import com.order.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlaceProductUseCase placeProductUseCase;

    @Test
    void shouldPlaceProducts() throws Exception {
        ProductId p1 = ProductId.generate();
        ProductController.ProductPlacementBatchRequest request = new ProductController.ProductPlacementBatchRequest(
                UUID.randomUUID(),
                List.of(new ProductController.ProductPlacementRequest("Product A", 10, new BigDecimal("20.00")))
        );

        when(placeProductUseCase.execute(any())).thenReturn(List.of(p1));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productIds[0]").value(p1.value().toString()));
    }
}
