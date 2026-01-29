package dev.markozivkovic.spring_crud_generator_demo.persistance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.markozivkovic.spring_crud_generator_demo.exception.InvalidResourceStateException;
import dev.markozivkovic.spring_crud_generator_demo.exception.ResourceNotFoundException;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.repository.OrderRepository;


@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    
    @MockitoBean
    private OrderRepository orderRepository;

    private OrderService orderService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.orderRepository);
    }

    @BeforeEach
    void before() {
        orderService = new OrderService(this.orderRepository);
    }

        @Test
    void getById() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final Long orderId = orderTable.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(orderTable));

        final OrderTable result = this.orderService.getById(orderId);

        verifyOrder(result, orderTable);

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void getById_notFound() {

        final Long orderId = Instancio.create(Long.class);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.getById(orderId))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Order with id not found: %s", orderId)
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

        @Test
    void getAll() {
                final List<OrderTable> orderTables = Instancio.ofList(OrderTable.class)
                        .size(10)
                        .create();

        final Page<OrderTable> pageOrder = new PageImpl<>(orderTables);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(this.orderRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenReturn(pageOrder);

        final Page<OrderTable> results = this.orderService.getAll(pageNumber, pageSize);

        assertThat(results).isNotNull();

        results.getContent().forEach(result -> {

            final OrderTable orderTable = orderTables.stream()
                    .filter(obj -> obj.getOrderId().equals(result.getOrderId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyOrder(result, orderTable);
        });

        verify(this.orderRepository).findAll(PageRequest.of(pageNumber, pageSize));
    }

    
    @Test
    void create() {

        final OrderTable order = Instancio.create(OrderTable.class);

        when(this.orderRepository.saveAndFlush(any()))
                .thenReturn(order);

        final OrderTable result = this.orderService.create(
            order.getProduct(), order.getQuantity(), order.getUsers()
        );

        verifyOrder(result, order);

        verify(this.orderRepository).saveAndFlush(any());
    }
    
    @Test
    void updateById() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(this.orderRepository.saveAndFlush(any()))
                .thenReturn(order);

        final OrderTable result = this.orderService.updateById(
            orderId, order.getQuantity()
        );

        verifyOrder(result, order);

        verify(this.orderRepository).findById(orderId);
        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void updateById_notFound() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.updateById(orderId, order.getQuantity()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Order with id not found: %s", orderId)
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }
        
    @Test
    void deleteById() {

        final Long orderId = Instancio.create(Long.class);

        this.orderService.deleteById(orderId);

        verify(this.orderRepository).deleteById(orderId);
    }
    
    @Test
    void addProduct() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long orderId = order.getOrderId();
        order.setProduct(null);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(this.orderRepository.saveAndFlush(any()))
                .thenReturn(order);

        final OrderTable result = this.orderService.addProduct(
            orderId, productModel
        );

        verifyOrder(result, order);

        verify(this.orderRepository).findById(orderId);
        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void addProduct_notFound() {

        final Long orderId = Instancio.create(Long.class);
        final ProductModel productModel = Instancio.create(ProductModel.class);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.addProduct(orderId, productModel))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Order with id not found: %s", orderId)
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void addProduct_invalidResourceState() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final ProductModel productModel = order.getProduct();
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> this.orderService.addProduct(orderId, productModel))
                .isExactlyInstanceOf(InvalidResourceStateException.class)
                .hasMessage(
                    "Not possible to add product"
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void addUsers() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long orderId = order.getOrderId();
        

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(this.orderRepository.saveAndFlush(any()))
                .thenReturn(order);

        final OrderTable result = this.orderService.addUsers(
            orderId, userEntity
        );

        verifyOrder(result, order);

        verify(this.orderRepository).findById(orderId);
        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void addUsers_notFound() {

        final Long orderId = Instancio.create(Long.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.addUsers(orderId, userEntity))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Order with id not found: %s", orderId)
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void addUsers_invalidResourceState() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final UserEntity userEntity = order.getUsers().stream()
                .findAny()
                .orElseThrow();
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> this.orderService.addUsers(orderId, userEntity))
                .isExactlyInstanceOf(InvalidResourceStateException.class)
                .hasMessage(
                    "Not possible to add users"
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void removeProduct() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(this.orderRepository.saveAndFlush(any()))
                .thenReturn(order);

        final OrderTable result = this.orderService.removeProduct(orderId);

        verifyOrder(result, order);

        verify(this.orderRepository).findById(orderId);
        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void removeProduct_notFound() {

        final Long orderId = Instancio.create(Long.class);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.removeProduct(orderId))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Order with id not found: %s", orderId)
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void removeProduct_invalidResourceState() {

        final OrderTable order = Instancio.create(OrderTable.class);
        order.setProduct(null);
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> this.orderService.removeProduct(orderId))
                .isExactlyInstanceOf(InvalidResourceStateException.class)
                .hasMessage(
                    "Not possible to remove product"
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void removeUsers() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final UserEntity userEntity =  order.getUsers().stream()
                .findAny()
                .orElseThrow();
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
        when(this.orderRepository.saveAndFlush(any()))
                .thenReturn(order);

        final OrderTable result = this.orderService.removeUsers(orderId, userEntity);

        verifyOrder(result, order);

        verify(this.orderRepository).findById(orderId);
        verify(this.orderRepository).saveAndFlush(any());
    }

    @Test
    void removeUsers_notFound() {

        final Long orderId = Instancio.create(Long.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.orderService.removeUsers(orderId, userEntity))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Order with id not found: %s", orderId)
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }

    @Test
    void removeUsers_invalidResourceState() {

        final OrderTable order = Instancio.create(OrderTable.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long orderId = order.getOrderId();

        when(this.orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> this.orderService.removeUsers(orderId, userEntity))
                .isExactlyInstanceOf(InvalidResourceStateException.class)
                .hasMessage(
                    "Not possible to remove users"
                )
                .hasNoCause();

        verify(this.orderRepository).findById(orderId);
    }


    private void verifyOrder(final OrderTable result, final OrderTable orderTable) {

        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderTable.getOrderId());
        assertThat(result.getProduct()).isEqualTo(orderTable.getProduct());
        assertThat(result.getQuantity()).isEqualTo(orderTable.getQuantity());
        assertThat(result.getUsers()).containsAll(orderTable.getUsers());
    }
}