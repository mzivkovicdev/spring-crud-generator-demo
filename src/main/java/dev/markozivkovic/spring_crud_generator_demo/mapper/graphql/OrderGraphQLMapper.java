package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderTO;

@Mapper(uses = { ProductGraphQLMapper.class, UserGraphQLMapper.class })
public interface OrderGraphQLMapper {

    @Mapping(target = "users", source = "users", qualifiedByName = "simple")
    @Mapping(target = "product", qualifiedByName = "simple")
    OrderTO mapOrderTableToOrderTO(final OrderTable model);

    @Mapping(target = "users", qualifiedByName = "simple")
    @Mapping(target = "product", qualifiedByName = "simple")
    List<OrderTO> mapOrderTableToOrderTO(final List<OrderTable> model);

    @Named("simple")
    @Mapping(target = "users", source = "users", ignore = true)
    @Mapping(target = "product", qualifiedByName = "simple")
    OrderTO mapOrderTableToOrderTOSimple(final OrderTable model);

    @Named("simpleList")
    @IterableMapping(qualifiedByName = "simple")
    List<OrderTO> mapOrderTableToOrderTOSimple(final List<OrderTable> model);

    OrderTable mapOrderTOToOrderTable(final OrderTO transferObject);

    List<OrderTable> mapOrderTOToOrderTable(final List<OrderTO> transferObject);

    
}