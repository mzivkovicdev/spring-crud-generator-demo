package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrdersGet200Response;
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
class OrderGetMockMvcTest {

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
        verifyNoInteractions(this.orderBusinessService);
        verifyNoMoreInteractions(this.orderService);
    }

    @Test
    void ordersIdGet() throws Exception {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final Long orderId = orderTable.getOrderId();

        when(this.orderService.getById(orderId)).thenReturn(orderTable);

        final ResultActions resultActions = this.mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk());

        final OrderPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                OrderPayload.class
        );

        verifyOrder(result, orderTable);

        verify(this.orderService).getById(orderId);
    }

    @Test
    void ordersIdGet_invalidOrderIdFormat() throws Exception {

        final String orderId = Instancio.create(String.class);

        this.mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ordersGet() throws Exception {
        final List<OrderTable> orderTables = Instancio.ofList(OrderTable.class)
                        .size(10)
                        .create();
        final Page<OrderTable> pageOrderTables = new PageImpl<>(orderTables);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(this.orderService.getAll(pageNumber, pageSize)).thenReturn(pageOrderTables);

        final ResultActions resultActions = this.mockMvc.perform(get("/api/orders")
                                .queryParam("pageNumber", String.format("%s", pageNumber))
                                .queryParam("pageSize", String.format("%s", pageSize)))
                        .andExpect(status().isOk());

        final OrdersGet200Response results = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                OrdersGet200Response.class
        );

        assertThat(results).isNotNull();
        assertThat(results.getTotalPages()).isNotNegative();
        assertThat(results.getTotalElements()).isNotNegative();
        assertThat(results.getSize()).isNotNegative();
        assertThat(results.getNumber()).isNotNegative();
        assertThat(results.getContent()).isNotEmpty();

        results.getContent().forEach(result -> {

            final OrderTable orderTable = orderTables.stream()
                    .filter(obj -> obj.getOrderId().toString().equals(result.getOrderId().toString()))
                    .findFirst()
                    .orElseThrow();

            verifyOrderSimple(result, orderTable);
        });

        verify(this.orderService).getAll(pageNumber, pageSize);
    }

    @Test
    void ordersGet_missingPageNumberParameter() throws Exception {

        final Integer pageSize = Instancio.create(Integer.class);

        this.mockMvc.perform(get("/api/orders")
                                .queryParam("pageSize", String.format("%s", pageSize)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void ordersGet_missingPageSizeParameter() throws Exception {

        final Integer pageNumber = Instancio.create(Integer.class);

        this.mockMvc.perform(get("/api/orders")
                                .queryParam("pageNumber", String.format("%s", pageNumber)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void ordersGet_typeMissmatch() throws Exception {

        final String pageNumber = Instancio.create(String.class);
        final String pageSize = Instancio.create(String.class);

        this.mockMvc.perform(get("/api/orders")
                                .queryParam("pageSize", String.format("%s", pageSize))
                                .queryParam("pageNumber", String.format("%s", pageNumber)))
                        .andExpect(status().isBadRequest());
    }

    private void verifyOrder(final OrderPayload result, final OrderTable orderTable) {
        
        assertThat(result).isNotNull();
        final OrderPayload mappedOrderTable = orderRestMapper.mapOrderTOToOrderPayload(
                orderRestMapper.mapOrderTableToOrderTO(orderTable)
        );
        assertThat(result).isEqualTo(mappedOrderTable);
    }

    private void verifyOrderSimple(final OrderPayload result, final OrderTable orderTable) {
        
        assertThat(result).isNotNull();
        final OrderPayload mappedOrderTable = orderRestMapper.mapOrderTOToOrderPayload(
                orderRestMapper.mapOrderTableToOrderTOSimple(orderTable)
        );
        assertThat(result).isEqualTo(mappedOrderTable);
    }
}