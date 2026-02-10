package dev.markozivkovic.spring_crud_generator_demo.persistance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.repository.ProductRepository;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Get a {@link ProductModel} by id.
     *
     * @param id The unique identifier for the product
     * @return Found ProductModel {@link ProductModel}
     */
    @Cacheable(value = "productModel", key = "#id")
    public ProductModel getById(final Long id) {

        return this.repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Product with id not found: %s", id)
            ));
    }
        
    /**
     * Get all {@link ProductModel} with pagination by page number and page size.
     *
     * @param pageNumber The page number.
     * @param pageSize The page size.
     * @return A page of {@link ProductModel}.
     */
    public Page<ProductModel> getAll(final Integer pageNumber, final Integer pageSize) {

        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }
        
    /**
     * Creates a new {@link ProductModel}.
     *
     * @param name The name of the product
     * @param price The price of the product
     * @param uuid The unique identifier for the product
     * @param birthDate The date and time the product was created
     * @param status The status of the product
     * @return the created {@link ProductModel}
     */
    @OptimisticLockingRetry
    @CachePut(value = "productModel", key = "#result.id")
    public ProductModel create(final String name, final String price, final List<UserEntity> users, final UUID uuid, final LocalDate birthDate, final StatusEnum status) {

        LOGGER.info("Creating new product");

        return this.repository.saveAndFlush(new ProductModel(name, price, users, uuid, birthDate, status));
    }
        
    /**
     * Updates an existing {@link ProductModel}
     *
     * @param id The unique identifier for the product
     * @param name The name of the product
     * @param price The price of the product
     * @param uuid The unique identifier for the product
     * @param birthDate The date and time the product was created
     * @param status The status of the product
     * @return updated {@link ProductModel}
     */
    @OptimisticLockingRetry
    @CachePut(value = "productModel", key = "#id")
    public ProductModel updateById(final Long id, final String name, final String price, final UUID uuid, final LocalDate birthDate, final StatusEnum status) {

        final ProductModel existing = this.getById(id);

        existing.setName(name)
            .setPrice(price)
            .setUuid(uuid)
            .setBirthDate(birthDate)
            .setStatus(status);

        LOGGER.info("Updating product with id {}", id);

        return this.repository.saveAndFlush(existing);
    }
        
    /**
    * Deletes a {@link ProductModel} by its ID.
    *
    * @param id The unique identifier for the product
    */
    @OptimisticLockingRetry
    @CacheEvict(value = "productModel", key = "#id")
    public void deleteById(final Long id) {

        LOGGER.info("Deleting product with id {}", id);

        this.repository.deleteById(id);

        LOGGER.info("Deleted product with id {}", id);
    }
        
    /**
     * Add {@link UserEntity} to {@link ProductModel}
     *
     * @param id The unique identifier for the product
     * @return Added {@link UserEntity} to {@link ProductModel}
     */
    @OptimisticLockingRetry
    @CachePut(value = "productModel", key = "#id")
    public ProductModel addUsers(final Long id, final UserEntity users) {

        final ProductModel entity = this.getById(id);
        
        if (entity.getUsers().contains(users) ||
                !entity.getUsers().add(users)) {
            throw new InvalidResourceStateException("Not possible to add users");
        }

        return this.repository.saveAndFlush(entity);
    }
    
    /**
     * Remove {@link UserEntity} from {@link ProductModel}
     *
     * @param id The unique identifier for the product
     * @return Removed {@link UserEntity} from {@link ProductModel}
     */
    @OptimisticLockingRetry
    @CachePut(value = "productModel", key = "#id")
    public ProductModel removeUsers(final Long id, final UserEntity users) {

        final ProductModel entity = this.getById(id);

        final boolean result = entity.getUsers()
                .removeIf(obj -> obj.getUserId().equals(users.getUserId()));
        if (!result) {
            throw new InvalidResourceStateException("Not possible to remove users");
        }

        return this.repository.saveAndFlush(entity);
    }

}