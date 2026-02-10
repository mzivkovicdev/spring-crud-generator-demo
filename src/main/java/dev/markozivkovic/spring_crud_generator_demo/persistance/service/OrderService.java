package dev.markozivkovic.spring_crud_generator_demo.persistance.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.markozivkovic.spring_crud_generator_demo.annotation.OptimisticLockingRetry;
import dev.markozivkovic.spring_crud_generator_demo.exception.InvalidResourceStateException;
import dev.markozivkovic.spring_crud_generator_demo.exception.ResourceNotFoundException;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;

    public OrderService(final OrderRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Get a {@link OrderTable} by id.
     *
     * @param id The unique identifier for the order
     * @return Found OrderTable {@link OrderTable}
     */
    @Cacheable(value = "orderTable", key = "#orderId")
    public OrderTable getById(final Long orderId) {

        return this.repository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Order with id not found: %s", orderId)
            ));
    }
        
    /**
     * Get all {@link OrderTable} with pagination by page number and page size.
     *
     * @param pageNumber The page number.
     * @param pageSize The page size.
     * @return A page of {@link OrderTable}.
     */
    public Page<OrderTable> getAll(final Integer pageNumber, final Integer pageSize) {

        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }
        
    /**
     * Creates a new {@link OrderTable}.
     *
     * @param product Represents a product
     * @return the created {@link OrderTable}
     */
    @OptimisticLockingRetry
    @CachePut(value = "orderTable", key = "#result.orderId")
    public OrderTable create(final ProductModel product, final Integer quantity, final List<UserEntity> users) {

        LOGGER.info("Creating new order");

        return this.repository.saveAndFlush(new OrderTable(product, quantity, users));
    }
        
    /**
     * Updates an existing {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @return updated {@link OrderTable}
     */
    @OptimisticLockingRetry
    @CachePut(value = "orderTable", key = "#orderId")
    public OrderTable updateById(final Long orderId, final Integer quantity) {

        final OrderTable existing = this.getById(orderId);

        existing.setQuantity(quantity);

        LOGGER.info("Updating order with id {}", orderId);

        return this.repository.saveAndFlush(existing);
    }
        
    /**
    * Deletes a {@link OrderTable} by its ID.
    *
    * @param orderId The unique identifier for the order
    */
    @OptimisticLockingRetry
    @CacheEvict(value = "orderTable", key = "#orderId")
    public void deleteById(final Long orderId) {

        LOGGER.info("Deleting order with id {}", orderId);

        this.repository.deleteById(orderId);

        LOGGER.info("Deleted order with id {}", orderId);
    }
        
    /**
     * Add {@link ProductModel} to {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @param product Represents a product
     * @return Added {@link ProductModel} to {@link OrderTable}
     */
    @OptimisticLockingRetry
    @CachePut(value = "orderTable", key = "#orderId")
    public OrderTable addProduct(final Long orderId, final ProductModel product) {

        final OrderTable entity = this.getById(orderId);
        
        if (entity.getProduct() != null) {
            throw new InvalidResourceStateException("Not possible to add product");
        }
        entity.setProduct(product);

        return this.repository.saveAndFlush(entity);
    }
    
    /**
     * Add {@link UserEntity} to {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @return Added {@link UserEntity} to {@link OrderTable}
     */
    @OptimisticLockingRetry
    @CachePut(value = "orderTable", key = "#orderId")
    public OrderTable addUsers(final Long orderId, final UserEntity users) {

        final OrderTable entity = this.getById(orderId);
        
        if (entity.getUsers().contains(users) ||
                !entity.getUsers().add(users)) {
            throw new InvalidResourceStateException("Not possible to add users");
        }

        return this.repository.saveAndFlush(entity);
    }
    
    /**
     * Remove {@link ProductModel} from {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @param product Represents a product
     * @return Removed {@link ProductModel} from {@link OrderTable}
     */
    @OptimisticLockingRetry
    @CachePut(value = "orderTable", key = "#orderId")
    public OrderTable removeProduct(final Long orderId) {

        final OrderTable entity = this.getById(orderId);

        if (entity.getProduct() == null) {
            throw new InvalidResourceStateException("Not possible to remove product");
        }
        entity.setProduct(null);

        return this.repository.saveAndFlush(entity);
    }
    
    /**
     * Remove {@link UserEntity} from {@link OrderTable}
     *
     * @param orderId The unique identifier for the order
     * @return Removed {@link UserEntity} from {@link OrderTable}
     */
    @OptimisticLockingRetry
    @CachePut(value = "orderTable", key = "#orderId")
    public OrderTable removeUsers(final Long orderId, final UserEntity users) {

        final OrderTable entity = this.getById(orderId);

        final boolean result = entity.getUsers()
                .removeIf(obj -> obj.getUserId().equals(users.getUserId()));
        if (!result) {
            throw new InvalidResourceStateException("Not possible to remove users");
        }

        return this.repository.saveAndFlush(entity);
    }

}