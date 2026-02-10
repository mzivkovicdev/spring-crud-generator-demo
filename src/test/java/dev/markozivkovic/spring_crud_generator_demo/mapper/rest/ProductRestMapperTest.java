package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.ProductTO;

class ProductRestMapperTest {

    private final ProductRestMapper productMapper = Mappers.getMapper(ProductRestMapper.class);

    @Test
    void mapProductModelToProductTO() {

        final ProductModel productModel = Instancio.create(ProductModel.class);

        final ProductTO result = this.productMapper.mapProductModelToProductTO(productModel);

        verifyProductTO(result, productModel);
    }

    @Test
    void mapProductModelToProductTO_list() {

        final List<ProductModel> productModels = Instancio.ofList(ProductModel.class)
                .size(10)
                .create();

        final List<ProductTO> results = this.productMapper.mapProductModelToProductTO(productModels);

        results.forEach(result -> {

            final ProductModel productModel = productModels.stream()
                    .filter(obj -> obj.getId().equals(result.id()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductTO(result, productModel);
        });
    }
    @Test
    void mapProductModelToProductTOSimple() {

        final ProductModel productModel = Instancio.create(ProductModel.class);

        final ProductTO result = this.productMapper.mapProductModelToProductTOSimple(productModel);

        verifyProductTOSimple(result, productModel);
    }

    @Test
    void mapProductModelToProductTOSimple_list() {

        final List<ProductModel> productModels = Instancio.ofList(ProductModel.class)
                .size(10)
                .create();

        final List<ProductTO> results = this.productMapper.mapProductModelToProductTOSimple(productModels);

        results.forEach(result -> {

            final ProductModel productModel = productModels.stream()
                    .filter(obj -> obj.getId().equals(result.id()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductTOSimple(result, productModel);
        });
    }
    
    @Test
    void mapProductTOToProductModel() {

        final ProductTO productTO = Instancio.create(ProductTO.class);

        final ProductModel result = this.productMapper.mapProductTOToProductModel(productTO);

        verifyProductModel(result, productTO);
    }

    @Test
    void mapProductTOToProductModel_list() {

        final List<ProductTO> productTOs = Instancio.ofList(ProductTO.class)
                .size(10)
                .create();

        final List<ProductModel> results = this.productMapper.mapProductTOToProductModel(productTOs);

        results.forEach(result -> {

            final ProductTO productTO = productTOs.stream()
                    .filter(product -> product.id().equals(result.getId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductModel(result, productTO);
        });
    }

    @Test
    void mapProductTOToProductPayload() {

        final ProductTO productTO = Instancio.create(ProductTO.class);

        final ProductPayload result = this.productMapper.mapProductTOToProductPayload(productTO);

        verifyProductPayload(result, productTO);
    }

    @Test
    void mapProductTOToProductPayload_list() {

        final List<ProductTO> productTOs = Instancio.ofList(ProductTO.class)
                .size(10)
                .create();

        final List<ProductPayload> results = this.productMapper.mapProductTOToProductPayload(productTOs);

        results.forEach(result -> {

            final ProductTO productTO = productTOs.stream()
                    .filter(product -> product.id().equals(result.getId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductPayload(result, productTO);
        });
    }

    private void verifyProductTO(final ProductTO result, final ProductModel productModel) {

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(productModel.getId());
        assertThat(result.name()).isEqualTo(productModel.getName());
        assertThat(result.price()).isEqualTo(productModel.getPrice());
        assertThat(result.uuid()).isEqualTo(productModel.getUuid());
        assertThat(result.birthDate()).isEqualTo(productModel.getBirthDate());
        assertThat(result.status()).isEqualTo(productModel.getStatus());
    }

    private void verifyProductTOSimple(final ProductTO result, final ProductModel productModel) {

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(productModel.getId());
        assertThat(result.name()).isEqualTo(productModel.getName());
        assertThat(result.price()).isEqualTo(productModel.getPrice());
        assertThat(result.uuid()).isEqualTo(productModel.getUuid());
        assertThat(result.birthDate()).isEqualTo(productModel.getBirthDate());
        assertThat(result.status()).isEqualTo(productModel.getStatus());
    }

    private void verifyProductModel(final ProductModel result, final ProductTO productTO) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productTO.id());
        assertThat(result.getName()).isEqualTo(productTO.name());
        assertThat(result.getPrice()).isEqualTo(productTO.price());
        assertThat(result.getUuid()).isEqualTo(productTO.uuid());
        assertThat(result.getBirthDate()).isEqualTo(productTO.birthDate());
        assertThat(result.getStatus()).isEqualTo(productTO.status());
    }

    private void verifyProductPayload(final ProductPayload result, final ProductTO productTO) {

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productTO.id());
        assertThat(result.getName()).isEqualTo(productTO.name());
        assertThat(result.getPrice()).isEqualTo(productTO.price());
        assertThat(result.getUuid()).isEqualTo(productTO.uuid());
        assertThat(result.getBirthDate()).isEqualTo(productTO.birthDate());
        assertThat(result.getStatus().name()).isEqualTo(productTO.status().name());
    }
}