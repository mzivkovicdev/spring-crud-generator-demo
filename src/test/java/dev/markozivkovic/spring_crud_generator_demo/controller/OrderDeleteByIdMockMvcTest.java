package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;


@WebMvcTest(excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        GlobalRestExceptionHandler.class, OrderController.class
})
class OrderDeleteByIdMockMvcTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderBusinessService orderBusinessService;
    
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void after() {
        verifyNoInteractions(this.orderBusinessService);
        verifyNoMoreInteractions(this.orderService);
    }

    @Test
    void ordersIdDelete() throws Exception {

        final Long orderId = Instancio.create(Long.class);

        this.mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isNoContent());

        verify(this.orderService).deleteById(orderId);
    }

    @Test
    void ordersIdDelete_invalidOrderIdFormat() throws Exception {

        final String orderId = Instancio.create(String.class);

        this.mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isBadRequest());
    }
}