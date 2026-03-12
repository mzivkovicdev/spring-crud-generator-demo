package dev.markozivkovic.spring_crud_generator_demo.businessservice;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.markozivkovic.spring_crud_generator_demo.annotation.OptimisticLockingRetry;
import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.ProductDetails;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;

@Service
public class ProductBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBusinessService.class);

    private final UserService userService;
    private final ProductService productService;

    public ProductBusinessService(final UserService userService, final ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
    
    @OptimisticLockingRetry
    public ProductModel create(final String name, final Integer price, final Set<Long> userIds, final UUID uuid, final LocalDate releaseDate, final List<ProductDetails> details, final StatusEnum status) {

        final Set<UserEntity> userEntitys = new HashSet<>(this.userService.getAllByIds(userIds.stream().toList()));

        return this.productService.create(name, price, userEntitys, uuid, releaseDate, details, status);
    }

    /**
     * Add {@link UserEntity} to {@link ProductModel}
     *
     * @param id The unique identifier for the product
     * @return Added {@link UserEntity} to {@link ProductModel}
     */
    @OptimisticLockingRetry
    public ProductModel addUsers(final Long id, final Long userId) {

        final UserEntity entity = this.userService.getById(userId);

        LOGGER.info("Adding UserEntity with ID {} to ProductModel with ID {}", userId, id);

        return this.productService.addUsers(id, entity);
    }
    
    /**
     * Add {@link UserEntity} to {@link ProductModel}
     *
     * @param id The unique identifier for the product
     * @return Added {@link UserEntity} to {@link ProductModel}
     */
    @OptimisticLockingRetry
    public ProductModel removeUsers(final Long id, final Long userId) {

        final UserEntity entity = this.userService.getById(userId);

        LOGGER.info("Removing UserEntity with ID {} from ProductModel with ID {}", userId, id);

        return this.productService.removeUsers(id, entity);
    }
}