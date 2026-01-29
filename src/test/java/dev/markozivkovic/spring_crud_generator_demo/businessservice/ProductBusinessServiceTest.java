package dev.markozivkovic.spring_crud_generator_demo.businessservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;


@ExtendWith(SpringExtension.class)
class ProductBusinessServiceTest {


    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ProductService productService;

    private ProductBusinessService productBusinessService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(
            this.userService, this.productService
        );
    }

    @BeforeEach
    void before() {
        productBusinessService = new ProductBusinessService(
            this.userService, this.productService
        );
    }

    
    @Test
    void create() {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final List<UserEntity> userEntitys = productModel.getUsers();
        final List<Long> userIds = userEntitys.stream()
                .map(UserEntity::getUserId)
                .toList();
        final String name = productModel.getName();
        final String price = productModel.getPrice();
        final UUID uuid = productModel.getUuid();
        final LocalDate birthDate = productModel.getBirthDate();
        final StatusEnum status = productModel.getStatus();

        when(this.userService.getAllByIds(userIds)).thenReturn(userEntitys);
        when(this.productService.create(name, price, userEntitys, uuid, birthDate, status)).thenReturn(productModel);

        final ProductModel result = this.productBusinessService.create(name, price, userIds, uuid, birthDate, status);

        verify(this.userService).getAllByIds(userIds);
        verify(this.productService).create(name, price, userEntitys, uuid, birthDate, status);

        verifyProduct(result, productModel);
    }
    
    @Test
    void addUsers() {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(this.userService.getReferenceById(userId)).thenReturn(userEntity);
        when(this.productService.addUsers(id, userEntity)).thenReturn(productModel);

        final ProductModel result = this.productBusinessService.addUsers(id, userId);

        verify(this.userService).getReferenceById(userId);
        verify(this.productService).addUsers(id, userEntity);

        verifyProduct(result, productModel);
    }

        @Test
    void removeUsers() {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(this.userService.getReferenceById(userId)).thenReturn(userEntity);
        when(this.productService.removeUsers(id, userEntity)).thenReturn(productModel);

        final ProductModel result = this.productBusinessService.removeUsers(id, userId);

        verify(this.userService).getReferenceById(userId);
        verify(this.productService).removeUsers(id, userEntity);

        verifyProduct(result, productModel);
    }


    private void verifyProduct(final ProductModel result, final ProductModel productModel) {

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(productModel);
    }
}