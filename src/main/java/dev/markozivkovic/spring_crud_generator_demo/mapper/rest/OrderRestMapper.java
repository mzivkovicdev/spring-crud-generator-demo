package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.OrderTO;

@Mapper(uses = { ProductRestMapper.class, UserRestMapper.class })
public interface OrderRestMapper {

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


    OrderPayload mapOrderTOToOrderPayload(final OrderTO transferObject);

    List<OrderPayload> mapOrderTOToOrderPayload(final List<OrderTO> transferObject);

    
}