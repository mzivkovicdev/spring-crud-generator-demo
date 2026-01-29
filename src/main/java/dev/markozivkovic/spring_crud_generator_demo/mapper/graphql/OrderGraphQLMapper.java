package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import java.util.List;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderTO;

@Mapper(uses = { ProductGraphQLMapper.class, UserGraphQLMapper.class })
public interface OrderGraphQLMapper {

    OrderTO mapOrderTableToOrderTO(final OrderTable model);

    List<OrderTO> mapOrderTableToOrderTO(final List<OrderTable> model);

    OrderTable mapOrderTOToOrderTable(final OrderTO transferObject);

    List<OrderTable> mapOrderTOToOrderTable(final List<OrderTO> transferObject);
    
}