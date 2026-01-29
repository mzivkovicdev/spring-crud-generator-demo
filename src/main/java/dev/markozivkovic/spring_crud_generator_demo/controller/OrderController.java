package dev.markozivkovic.spring_crud_generator_demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.api.OrdersApi;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderCreatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrderUpdatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.OrdersGet200Response;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.ProductInput;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.UserInput;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.OrderRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController implements OrdersApi {

    private final OrderRestMapper orderMapper = Mappers.getMapper(OrderRestMapper.class);

    private final OrderService orderService;
    private final OrderBusinessService orderBusinessService;

    public OrderController(final OrderService orderService, final OrderBusinessService orderBusinessService) {
        this.orderService = orderService;
        this.orderBusinessService = orderBusinessService;
    }

        @Override
        public ResponseEntity<OrderPayload> ordersPost(final OrderCreatePayload body) {

        final Long productId = body.getProduct() != null ? body.getProduct().getId() : null;
        final List<Long> usersIds = (body.getUsers() != null && !body.getUsers().isEmpty()) ? 
                body.getUsers().stream()
                    .map(UserInput::getUserId)
                    .collect(Collectors.toList()) :
                List.of();

        return ResponseEntity.ok(
            orderMapper.mapOrderTOToOrderPayload(
                orderMapper.mapOrderTableToOrderTO(
                    this.orderBusinessService.create(
                        productId, body.getQuantity(), usersIds
                    )
                )
            )
        );
    
    }
    
    @Override
    public ResponseEntity<OrderPayload> ordersIdGet(final Long id) {
        return ResponseEntity.ok(
            orderMapper.mapOrderTOToOrderPayload(
                orderMapper.mapOrderTableToOrderTO(
                    this.orderService.getById(id)
                )
            )
        );
    }
    
    @Override
    public ResponseEntity<OrdersGet200Response> ordersGet(final Integer pageNumber, final Integer pageSize) {

        final Page<OrderTable> pageObject = this.orderService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(
            new OrdersGet200Response()
                .totalPages(pageObject.getTotalPages())
                .totalElements(pageObject.getTotalElements())
                .size(pageObject.getSize())
                .number(pageObject.getNumber())
                .content(
                    orderMapper.mapOrderTOToOrderPayload(
                        orderMapper.mapOrderTableToOrderTO(
                            pageObject.getContent()
                        )
                    )
                )
        );
    }
    
    @Override
    public ResponseEntity<OrderPayload> ordersIdPut(final Long id, final OrderUpdatePayload body) {
        return ResponseEntity.ok(
            orderMapper.mapOrderTOToOrderPayload(
                orderMapper.mapOrderTableToOrderTO(
                    this.orderService.updateById(id, body.getQuantity())
                )
            )
        );
    }
        
    @Override
    public ResponseEntity<Void> ordersIdDelete(final Long id) {

        this.orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<OrderPayload> ordersIdProductsPost(final Long id,
            final ProductInput body) {
        return ResponseEntity.ok(
            orderMapper.mapOrderTOToOrderPayload(
                orderMapper.mapOrderTableToOrderTO(
                    this.orderBusinessService.addProduct(id, body.getId())
                )
            )
        );
    }

    @Override
    public ResponseEntity<OrderPayload> ordersIdUsersPost(final Long id,
            final UserInput body) {
        return ResponseEntity.ok(
            orderMapper.mapOrderTOToOrderPayload(
                orderMapper.mapOrderTableToOrderTO(
                    this.orderBusinessService.addUsers(id, body.getUserId())
                )
            )
        );
    }
    
    @Override
    public ResponseEntity<Void> ordersIdProductsDelete(final Long id) {

        this.orderService.removeProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> ordersIdUsersDelete(final Long id, final Long userId) {

        this.orderBusinessService.removeUsers(id, userId);
        return ResponseEntity.noContent().build();
    }

}