package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderCreatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.UserInput;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.OrderRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;

import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        GlobalRestExceptionHandler.class, OrderController.class
})
class OrderCreateMockMvcTest {

    private final OrderRestMapper orderRestMapper = Mappers.getMapper(OrderRestMapper.class);

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderBusinessService orderBusinessService;
    
    @Autowired
    private JsonMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.orderBusinessService);
        verifyNoInteractions(this.orderService);
    }

    @Test
    void ordersPost() throws Exception {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final OrderCreatePayload body = Instancio.create(OrderCreatePayload.class);
        final Long productId = body.getProduct() != null ? body.getProduct().getId() : null;
        final List<Long> usersIds = (body.getUsers() != null && !body.getUsers().isEmpty()) ? 
                body.getUsers().stream()
                    .map(UserInput::getUserId)
                    .toList() : 
                List.of();

        when(this.orderBusinessService.create(
                productId, body.getQuantity(), usersIds
        )).thenReturn(orderTable);

        final ResultActions resultActions = this.mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        final OrderPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                OrderPayload.class
        );

        verifyOrder(result, orderTable);

        verify(this.orderBusinessService).create(
                productId, body.getQuantity(), usersIds
        );
    }

    @Test
    void ordersPost_noRequestBody() throws Exception {

        this.mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    private void verifyOrder(final OrderPayload result, final OrderTable orderTable) {
        
        assertThat(result).isNotNull();
        final OrderPayload mappedOrderTable = orderRestMapper.mapOrderTOToOrderPayload(
                orderRestMapper.mapOrderTableToOrderTO(orderTable)
        );
        assertThat(result).isEqualTo(mappedOrderTable);
    }
}