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

import dev.markozivkovic.spring_crud_generator_demo.exception.ResourceNotFoundException;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.repository.UserRepository;


@ExtendWith(SpringExtension.class)
class UserServiceTest {
    
    @MockitoBean
    private UserRepository userRepository;

    private UserService userService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.userRepository);
    }

    @BeforeEach
    void before() {
        userService = new UserService(this.userRepository);
    }

        @Test
    void getById() {

        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(this.userRepository.findById(userId))
                .thenReturn(Optional.of(userEntity));

        final UserEntity result = this.userService.getById(userId);

        verifyUser(result, userEntity);

        verify(this.userRepository).findById(userId);
    }

    @Test
    void getById_notFound() {

        final Long userId = Instancio.create(Long.class);

        when(this.userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.userService.getById(userId))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("User with id not found: %s", userId)
                )
                .hasNoCause();

        verify(this.userRepository).findById(userId);
    }

        @Test
    void getAll() {
                final List<UserEntity> userEntitys = Instancio.ofList(UserEntity.class)
                        .size(10)
                        .create();

        final Page<UserEntity> pageUser = new PageImpl<>(userEntitys);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(this.userRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenReturn(pageUser);

        final Page<UserEntity> results = this.userService.getAll(pageNumber, pageSize);

        assertThat(results).isNotNull();

        results.getContent().forEach(result -> {

            final UserEntity userEntity = userEntitys.stream()
                    .filter(obj -> obj.getUserId().equals(result.getUserId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyUser(result, userEntity);
        });

        verify(this.userRepository).findAll(PageRequest.of(pageNumber, pageSize));
    }

    
    @Test
    void create() {

        final UserEntity user = Instancio.create(UserEntity.class);

        when(this.userRepository.saveAndFlush(any()))
                .thenReturn(user);

        final UserEntity result = this.userService.create(
            user.getUsername(), user.getEmail(), user.getPassword(), user.getDetails(), user.getRoles(), user.getPermissions()
        );

        verifyUser(result, user);

        verify(this.userRepository).saveAndFlush(any());
    }
    
    @Test
    void updateById() {

        final UserEntity user = Instancio.create(UserEntity.class);
        final Long userId = user.getUserId();

        when(this.userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(this.userRepository.saveAndFlush(any()))
                .thenReturn(user);

        final UserEntity result = this.userService.updateById(
            userId, user.getUsername(), user.getEmail(), user.getPassword(), user.getDetails(), user.getRoles(), user.getPermissions()
        );

        verifyUser(result, user);

        verify(this.userRepository).findById(userId);
        verify(this.userRepository).saveAndFlush(any());
    }

    @Test
    void updateById_notFound() {

        final UserEntity user = Instancio.create(UserEntity.class);
        final Long userId = user.getUserId();

        when(this.userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.userService.updateById(userId, user.getUsername(), user.getEmail(), user.getPassword(), user.getDetails(), user.getRoles(), user.getPermissions()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("User with id not found: %s", userId)
                )
                .hasNoCause();

        verify(this.userRepository).findById(userId);
    }
        
    @Test
    void deleteById() {

        final Long userId = Instancio.create(Long.class);

        this.userService.deleteById(userId);

        verify(this.userRepository).deleteById(userId);
    }
    
    @Test
    void getAllByIds() {
                final List<UserEntity> users = Instancio.ofList(UserEntity.class)
                        .size(10)
                        .create();

        final List<Long> ids = users.stream()
                .map(UserEntity::getUserId)
                .toList();

        when(this.userRepository.findAllById(ids))
                .thenReturn(users);

        final List<UserEntity> results = this.userService.getAllByIds(ids);

        results.forEach(result -> {

            final UserEntity user = users.stream()
                    .filter(obj -> obj.getUserId().equals(result.getUserId()))
                    .findFirst()
                    .orElseThrow();

            verifyUser(result, user);
        });

        verify(this.userRepository).findAllById(ids);
    }

    private void verifyUser(final UserEntity result, final UserEntity userEntity) {

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(result.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(result.getDetails()).isEqualTo(userEntity.getDetails());
        assertThat(result.getRoles()).isEqualTo(userEntity.getRoles());
        assertThat(result.getPermissions()).isEqualTo(userEntity.getPermissions());
    }
}