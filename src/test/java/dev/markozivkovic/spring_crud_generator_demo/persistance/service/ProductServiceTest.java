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
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.repository.ProductRepository;


@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    
    @MockitoBean
    private ProductRepository productRepository;

    private ProductService productService;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.productRepository);
    }

    @BeforeEach
    void before() {
        productService = new ProductService(this.productRepository);
    }

        @Test
    void getById() {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();

        when(this.productRepository.findById(id))
                .thenReturn(Optional.of(productModel));

        final ProductModel result = this.productService.getById(id);

        verifyProduct(result, productModel);

        verify(this.productRepository).findById(id);
    }

    @Test
    void getById_notFound() {

        final Long id = Instancio.create(Long.class);

        when(this.productRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.getById(id))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Product with id not found: %s", id)
                )
                .hasNoCause();

        verify(this.productRepository).findById(id);
    }

        @Test
    void getAll() {
                final List<ProductModel> productModels = Instancio.ofList(ProductModel.class)
                        .size(10)
                        .create();

        final Page<ProductModel> pageProduct = new PageImpl<>(productModels);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(this.productRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenReturn(pageProduct);

        final Page<ProductModel> results = this.productService.getAll(pageNumber, pageSize);

        assertThat(results).isNotNull();

        results.getContent().forEach(result -> {

            final ProductModel productModel = productModels.stream()
                    .filter(obj -> obj.getId().equals(result.getId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProduct(result, productModel);
        });

        verify(this.productRepository).findAll(PageRequest.of(pageNumber, pageSize));
    }

    
    @Test
    void create() {

        final ProductModel product = Instancio.create(ProductModel.class);

        when(this.productRepository.saveAndFlush(any()))
                .thenReturn(product);

        final ProductModel result = this.productService.create(
            product.getName(), product.getPrice(), product.getUsers(), product.getUuid(), product.getBirthDate(), product.getStatus()
        );

        verifyProduct(result, product);

        verify(this.productRepository).saveAndFlush(any());
    }
    
    @Test
    void updateById() {

        final ProductModel product = Instancio.create(ProductModel.class);
        final Long id = product.getId();

        when(this.productRepository.findById(id))
                .thenReturn(Optional.of(product));
        when(this.productRepository.saveAndFlush(any()))
                .thenReturn(product);

        final ProductModel result = this.productService.updateById(
            id, product.getName(), product.getPrice(), product.getUuid(), product.getBirthDate(), product.getStatus()
        );

        verifyProduct(result, product);

        verify(this.productRepository).findById(id);
        verify(this.productRepository).saveAndFlush(any());
    }

    @Test
    void updateById_notFound() {

        final ProductModel product = Instancio.create(ProductModel.class);
        final Long id = product.getId();

        when(this.productRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.updateById(id, product.getName(), product.getPrice(), product.getUuid(), product.getBirthDate(), product.getStatus()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Product with id not found: %s", id)
                )
                .hasNoCause();

        verify(this.productRepository).findById(id);
    }
        
    @Test
    void deleteById() {

        final Long id = Instancio.create(Long.class);

        this.productService.deleteById(id);

        verify(this.productRepository).deleteById(id);
    }
    
    @Test
    void addUsers() {

        final ProductModel product = Instancio.create(ProductModel.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long id = product.getId();
        

        when(this.productRepository.findById(id))
                .thenReturn(Optional.of(product));
        when(this.productRepository.saveAndFlush(any()))
                .thenReturn(product);

        final ProductModel result = this.productService.addUsers(
            id, userEntity
        );

        verifyProduct(result, product);

        verify(this.productRepository).findById(id);
        verify(this.productRepository).saveAndFlush(any());
    }

    @Test
    void addUsers_notFound() {

        final Long id = Instancio.create(Long.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);

        when(this.productRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.addUsers(id, userEntity))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Product with id not found: %s", id)
                )
                .hasNoCause();

        verify(this.productRepository).findById(id);
    }

    @Test
    void addUsers_invalidResourceState() {

        final ProductModel product = Instancio.create(ProductModel.class);
        final UserEntity userEntity = product.getUsers().stream()
                .findAny()
                .orElseThrow();
        final Long id = product.getId();

        when(this.productRepository.findById(id))
                .thenReturn(Optional.of(product));

        assertThatThrownBy(() -> this.productService.addUsers(id, userEntity))
                .isExactlyInstanceOf(InvalidResourceStateException.class)
                .hasMessage(
                    "Not possible to add users"
                )
                .hasNoCause();

        verify(this.productRepository).findById(id);
    }

    @Test
    void removeUsers() {

        final ProductModel product = Instancio.create(ProductModel.class);
        final UserEntity userEntity =  product.getUsers().stream()
                .findAny()
                .orElseThrow();
        final Long id = product.getId();

        when(this.productRepository.findById(id))
                .thenReturn(Optional.of(product));
        when(this.productRepository.saveAndFlush(any()))
                .thenReturn(product);

        final ProductModel result = this.productService.removeUsers(id, userEntity);

        verifyProduct(result, product);

        verify(this.productRepository).findById(id);
        verify(this.productRepository).saveAndFlush(any());
    }

    @Test
    void removeUsers_notFound() {

        final Long id = Instancio.create(Long.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);

        when(this.productRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.productService.removeUsers(id, userEntity))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                    String.format("Product with id not found: %s", id)
                )
                .hasNoCause();

        verify(this.productRepository).findById(id);
    }

    @Test
    void removeUsers_invalidResourceState() {

        final ProductModel product = Instancio.create(ProductModel.class);
        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long id = product.getId();

        when(this.productRepository.findById(id))
                .thenReturn(Optional.of(product));

        assertThatThrownBy(() -> this.productService.removeUsers(id, userEntity))
                .isExactlyInstanceOf(InvalidResourceStateException.class)
                .hasMessage(
                    "Not possible to remove users"
                )
                .hasNoCause();

        verify(this.productRepository).findById(id);
    }


    private void verifyProduct(final ProductModel result, final ProductModel productModel) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productModel.getId());
        assertThat(result.getName()).isEqualTo(productModel.getName());
        assertThat(result.getPrice()).isEqualTo(productModel.getPrice());
        assertThat(result.getUuid()).isEqualTo(productModel.getUuid());
        assertThat(result.getBirthDate()).isEqualTo(productModel.getBirthDate());
        assertThat(result.getStatus()).isEqualTo(productModel.getStatus());
        assertThat(result.getUsers()).containsAll(productModel.getUsers());
    }
}