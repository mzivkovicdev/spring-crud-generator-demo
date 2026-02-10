package dev.markozivkovic.spring_crud_generator_demo.businessservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;


import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;


@ExtendWith(SpringExtension.class)
class OrderBusinessServiceTest {


    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private OrderService orderService;

    private OrderBusinessService orderBusinessService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(
            this.productService, this.userService, this.orderService
        );
    }

    @BeforeEach
    void before() {
        orderBusinessService = new OrderBusinessService(
            this.productService, this.userService, this.orderService
        );
    }

    
    @Test
    void create() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final ProductModel productModel = orderTable.getProduct();
        final Long productId = productModel.getId();
        final List<UserEntity> userEntitys = orderTable.getUsers();
        final List<Long> userIds = userEntitys.stream()
                .map(UserEntity::getUserId)
                .toList();
        final Integer quantity = orderTable.getQuantity();

        when(this.productService.getById(productId)).thenReturn(productModel);
        when(this.userService.getAllByIds(userIds)).thenReturn(userEntitys);
        when(this.orderService.create(productModel, quantity, userEntitys)).thenReturn(orderTable);

        final OrderTable result = this.orderBusinessService.create(productId, quantity, userIds);

        verify(this.productService).getById(productId);
        verify(this.userService).getAllByIds(userIds);
        verify(this.orderService).create(productModel, quantity, userEntitys);

        verifyOrder(result, orderTable);
    }
    
    @Test
    void addProduct() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final Long orderId = orderTable.getOrderId();
        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long productId = productModel.getId();

        when(this.productService.getById(productId)).thenReturn(productModel);
        when(this.orderService.addProduct(orderId, productModel)).thenReturn(orderTable);

        final OrderTable result = this.orderBusinessService.addProduct(orderId, productId);

        verify(this.productService).getById(productId);
        verify(this.orderService).addProduct(orderId, productModel);

        verifyOrder(result, orderTable);
    }

    @Test
    void addUsers() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final Long orderId = orderTable.getOrderId();
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(this.userService.getById(userId)).thenReturn(userEntity);
        when(this.orderService.addUsers(orderId, userEntity)).thenReturn(orderTable);

        final OrderTable result = this.orderBusinessService.addUsers(orderId, userId);

        verify(this.userService).getById(userId);
        verify(this.orderService).addUsers(orderId, userEntity);

        verifyOrder(result, orderTable);
    }

    
    @Test
    void removeUsers() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final Long orderId = orderTable.getOrderId();
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(this.userService.getById(userId)).thenReturn(userEntity);
        when(this.orderService.removeUsers(orderId, userEntity)).thenReturn(orderTable);

        final OrderTable result = this.orderBusinessService.removeUsers(orderId, userId);

        verify(this.userService).getById(userId);
        verify(this.orderService).removeUsers(orderId, userEntity);

        verifyOrder(result, orderTable);
    }


    private void verifyOrder(final OrderTable result, final OrderTable orderTable) {

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(orderTable);
    }
}