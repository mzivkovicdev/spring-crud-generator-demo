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
import dev.markozivkovic.spring_crud_generator_demo.exception.ResourceNotFoundException;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.Details;
import dev.markozivkovic.spring_crud_generator_demo.persistance.repository.UserRepository;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }
    
    
    @Cacheable(value = "userEntity", key = "#userId")
    public UserEntity getById(final Long userId) {

        return this.repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id not found: %s", userId)
            ));
    }
        
    /**
     * Get all {@link UserEntity} with pagination by page number and page size.
     *
     * @param pageNumber The page number.
     * @param pageSize The page size.
     * @return A page of {@link UserEntity}.
     */
    public Page<UserEntity> getAll(final Integer pageNumber, final Integer pageSize) {

        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }
        
    @OptimisticLockingRetry
    @CachePut(value = "userEntity", key = "#result.userId")
    public UserEntity create(final String username, final String email, final String password, final Details details, final List<String> roles, final List<String> permissions) {

        LOGGER.info("Creating new user");

        return this.repository.saveAndFlush(new UserEntity(username, email, password, details, roles, permissions));
    }
        
    @OptimisticLockingRetry
    @CachePut(value = "userEntity", key = "#userId")
    public UserEntity updateById(final Long userId, final String username, final String email, final String password, final Details details, final List<String> roles, final List<String> permissions) {

        final UserEntity existing = this.getById(userId);

        existing.setUsername(username)
            .setEmail(email)
            .setPassword(password)
            .setDetails(details)
            .setRoles(roles)
            .setPermissions(permissions);

        LOGGER.info("Updating user with id {}", userId);

        return this.repository.saveAndFlush(existing);
    }
        
    @OptimisticLockingRetry
    @CacheEvict(value = "userEntity", key = "#userId")
    public void deleteById(final Long userId) {

        LOGGER.info("Deleting user with id {}", userId);

        this.repository.deleteById(userId);

        LOGGER.info("Deleted user with id {}", userId);
    }
    

    public List<UserEntity> getAllByIds(final List<Long> ids) {

        return this.repository.findAllById(ids);
    }

}