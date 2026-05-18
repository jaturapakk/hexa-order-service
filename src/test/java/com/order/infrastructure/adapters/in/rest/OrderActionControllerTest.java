package com.order.infrastructure.adapters.in.rest;

import com.order.application.ports.in.PayOrderUseCase;
import com.order.application.ports.in.ShipOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderActionController.class)
class OrderActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayOrderUseCase payOrderUseCase;

    @MockBean
    private ShipOrderUseCase shipOrderUseCase;

    @Test
    void shouldPayOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        doNothing().when(payOrderUseCase).execute(any());

        mockMvc.perform(post("/orders/" + orderId + "/pay"))
                .andExpect(status().isOk());

        verify(payOrderUseCase).execute(any());
    }

    @Test
    void shouldShipOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        doNothing().when(shipOrderUseCase).execute(any());

        mockMvc.perform(post("/orders/" + orderId + "/ship"))
                .andExpect(status().isOk());

        verify(shipOrderUseCase).execute(any());
    }
}
