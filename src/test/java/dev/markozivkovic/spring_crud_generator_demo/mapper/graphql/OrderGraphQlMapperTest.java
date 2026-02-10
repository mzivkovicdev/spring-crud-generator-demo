package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderTO;

class OrderGraphQlMapperTest {

    private final OrderGraphQLMapper orderMapper = Mappers.getMapper(OrderGraphQLMapper.class);

    @Test
    void mapOrderTableToOrderTO() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);

        final OrderTO result = this.orderMapper.mapOrderTableToOrderTO(orderTable);

        verifyOrderTO(result, orderTable);
    }

    @Test
    void mapOrderTableToOrderTO_list() {

        final List<OrderTable> orderTables = Instancio.ofList(OrderTable.class)
                .size(10)
                .create();

        final List<OrderTO> results = this.orderMapper.mapOrderTableToOrderTO(orderTables);

        results.forEach(result -> {

            final OrderTable orderTable = orderTables.stream()
                    .filter(obj -> obj.getOrderId().equals(result.orderId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyOrderTO(result, orderTable);
        });
    }
    @Test
    void mapOrderTableToOrderTOSimple() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);

        final OrderTO result = this.orderMapper.mapOrderTableToOrderTOSimple(orderTable);

        verifyOrderTOSimple(result, orderTable);
    }

    @Test
    void mapOrderTableToOrderTOSimple_list() {

        final List<OrderTable> orderTables = Instancio.ofList(OrderTable.class)
                .size(10)
                .create();

        final List<OrderTO> results = this.orderMapper.mapOrderTableToOrderTOSimple(orderTables);

        results.forEach(result -> {

            final OrderTable orderTable = orderTables.stream()
                    .filter(obj -> obj.getOrderId().equals(result.orderId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyOrderTOSimple(result, orderTable);
        });
    }
    
    @Test
    void mapOrderTOToOrderTable() {

        final OrderTO orderTO = Instancio.create(OrderTO.class);

        final OrderTable result = this.orderMapper.mapOrderTOToOrderTable(orderTO);

        verifyOrderTable(result, orderTO);
    }

    @Test
    void mapOrderTOToOrderTable_list() {

        final List<OrderTO> orderTOs = Instancio.ofList(OrderTO.class)
                .size(10)
                .create();

        final List<OrderTable> results = this.orderMapper.mapOrderTOToOrderTable(orderTOs);

        results.forEach(result -> {

            final OrderTO orderTO = orderTOs.stream()
                    .filter(order -> order.orderId().equals(result.getOrderId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyOrderTable(result, orderTO);
        });
    }


    private void verifyOrderTO(final OrderTO result, final OrderTable orderTable) {

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(orderTable.getOrderId());
        assertThat(result.quantity()).isEqualTo(orderTable.getQuantity());
    }

    private void verifyOrderTOSimple(final OrderTO result, final OrderTable orderTable) {

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(orderTable.getOrderId());
        assertThat(result.quantity()).isEqualTo(orderTable.getQuantity());
    }

    private void verifyOrderTable(final OrderTable result, final OrderTO orderTO) {

        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderTO.orderId());
        assertThat(result.getQuantity()).isEqualTo(orderTO.quantity());
    }

}