package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import java.util.List;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.OrderTO;

@Mapper(uses = { ProductRestMapper.class, UserRestMapper.class })
public interface OrderRestMapper {

    OrderTO mapOrderTableToOrderTO(final OrderTable model);

    List<OrderTO> mapOrderTableToOrderTO(final List<OrderTable> model);

    OrderTable mapOrderTOToOrderTable(final OrderTO transferObject);

    List<OrderTable> mapOrderTOToOrderTable(final List<OrderTO> transferObject);

    OrderPayload mapOrderTOToOrderPayload(final OrderTO transferObject);

    List<OrderPayload> mapOrderTOToOrderPayload(final List<OrderTO> transferObject);

    
}