package dev.markozivkovic.spring_crud_generator_demo.graphql;

import jakarta.validation.Valid;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.OrderGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.PageTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderCreateTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderUpdateTO;

@Controller
public class OrderResolver {

    private final OrderGraphQLMapper orderMapper = Mappers.getMapper(OrderGraphQLMapper.class);

    private final OrderService orderService;
    private final OrderBusinessService orderBusinessService;

    public OrderResolver(final OrderService orderService, final OrderBusinessService orderBusinessService) {
        this.orderService = orderService;
        this.orderBusinessService = orderBusinessService;
    }
    
    @QueryMapping
    public OrderTO orderById(@Argument final Long id) {
        return orderMapper.mapOrderTableToOrderTO(
            this.orderService.getById(id)
        );
    }

    @QueryMapping
    public PageTO<OrderTO> ordersPage(@Argument final Integer pageNumber,
                                    @Argument final Integer pageSize) {
        
        final Page<OrderTable> pageObject = this.orderService.getAll(pageNumber, pageSize);

        return new PageTO<>(
            pageObject.getTotalPages(),
            pageObject.getTotalElements(),
            pageObject.getSize(),
            pageObject.getNumber(),
            orderMapper.mapOrderTableToOrderTO(pageObject.getContent())
        );
    }
    
    @MutationMapping
    @Validated
    public OrderTO createOrder(@Argument @Valid final OrderCreateTO input) {
        return orderMapper.mapOrderTableToOrderTO(
            this.orderBusinessService.create(
                input.productId(), input.quantity(), input.usersIds()
            )
        );
    }

    @MutationMapping
    @Validated
    public OrderTO updateOrder(@Argument final Long id, @Argument @Valid final OrderUpdateTO input) {

        return orderMapper.mapOrderTableToOrderTO(
                this.orderService.updateById(id, input.quantity())
        );
    }

    @MutationMapping
    public boolean deleteOrder(@Argument final Long id) {
        
        this.orderService.deleteById(id);
        
        return true;
    }

    @MutationMapping
    public OrderTO addProductToOrder(@Argument final Long id, @Argument final Long productId) {
        return orderMapper.mapOrderTableToOrderTO(
            this.orderBusinessService.addProduct(id, productId)
        );
    }

    @MutationMapping
    public OrderTO removeProductFromOrder(@Argument final Long id) {

        return orderMapper.mapOrderTableToOrderTO(
            this.orderService.removeProduct(id)
        );
    }

    @MutationMapping
    public OrderTO addUsersToOrder(@Argument final Long id, @Argument final Long usersId) {
        return orderMapper.mapOrderTableToOrderTO(
            this.orderBusinessService.addUsers(id, usersId)
        );
    }

    @MutationMapping
    public OrderTO removeUsersFromOrder(@Argument final Long id, @Argument final Long usersId) {

        return orderMapper.mapOrderTableToOrderTO(
            this.orderBusinessService.removeUsers(id, usersId)
        );
    }

}