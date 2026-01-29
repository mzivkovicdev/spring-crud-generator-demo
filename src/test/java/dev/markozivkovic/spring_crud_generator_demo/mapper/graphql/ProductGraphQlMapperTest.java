package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductTO;

class ProductGraphQlMapperTest {

    private final ProductGraphQLMapper productMapper = Mappers.getMapper(ProductGraphQLMapper.class);

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


    private void verifyProductTO(final ProductTO result, final ProductModel productModel) {

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

}