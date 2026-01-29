package dev.markozivkovic.spring_crud_generator_demo.businessservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.markozivkovic.spring_crud_generator_demo.annotation.OptimisticLockingRetry;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;

@Service
public class OrderBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBusinessService.class);

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public OrderBusinessService(final ProductService productService, final UserService userService, final OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }
    
    @OptimisticLockingRetry
    public OrderTable create(final Long productId, final Integer quantity, final List<Long> userIds) {

        final ProductModel productModel = productId != null ?
                this.productService.getReferenceById(productId) :
                null;
        final List<UserEntity> userEntitys = this.userService.getAllByIds(userIds);

        return this.orderService.create(productModel, quantity, userEntitys);
    }

    /**
     * Add {@link ProductModel} to {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @param product Represents a product
     * @return Added {@link ProductModel} to {@link OrderTable}
     */
    @OptimisticLockingRetry
    public OrderTable addProduct(final Long orderId, final Long productId) {

        final ProductModel entity = this.productService.getReferenceById(productId);

        LOGGER.info("Adding ProductModel with ID {} to OrderTable with ID {}", productId, orderId);

        return this.orderService.addProduct(orderId, entity);
    }

    /**
     * Add {@link UserEntity} to {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @return Added {@link UserEntity} to {@link OrderTable}
     */
    @OptimisticLockingRetry
    public OrderTable addUsers(final Long orderId, final Long userId) {

        final UserEntity entity = this.userService.getReferenceById(userId);

        LOGGER.info("Adding UserEntity with ID {} to OrderTable with ID {}", userId, orderId);

        return this.orderService.addUsers(orderId, entity);
    }
    
    /**
     * Add {@link UserEntity} to {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @return Added {@link UserEntity} to {@link OrderTable}
     */
    @OptimisticLockingRetry
    public OrderTable removeUsers(final Long orderId, final Long userId) {

        final UserEntity entity = this.userService.getReferenceById(userId);

        LOGGER.info("Removing UserEntity with ID {} from OrderTable with ID {}", userId, orderId);

        return this.orderService.removeUsers(orderId, entity);
    }
}