package dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductDetailsPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.ProductDetails;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.helpers.ProductDetailsTO;

class ProductDetailsRestMapperTest {

    private final ProductDetailsRestMapper productDetailsMapper = Mappers.getMapper(ProductDetailsRestMapper.class);

    @Test
    void mapProductDetailsToProductDetailsTO() {

        final ProductDetails productDetails = Instancio.create(ProductDetails.class);

        final ProductDetailsTO result = this.productDetailsMapper.mapProductDetailsToProductDetailsTO(productDetails);

        verifyProductDetailsTO(result, productDetails);
    }

    @Test
    void mapProductDetailsToProductDetailsTO_list() {

        final List<ProductDetails> productDetailss = Instancio.ofList(ProductDetails.class)
                .size(10)
                .create();

        final List<ProductDetailsTO> results = this.productDetailsMapper.mapProductDetailsToProductDetailsTO(productDetailss);

        results.forEach(result -> {

            final ProductDetails productDetails = productDetailss.stream()
                    .filter(obj -> obj.getDescription().equals(result.description()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductDetailsTO(result, productDetails);
        });
    }

    @Test
    void mapProductDetailsToProductDetailsTO_set() {

        final Set<ProductDetails> productDetailss = Instancio.ofSet(ProductDetails.class)
                .size(10)
                .create();

        final Set<ProductDetailsTO> results = this.productDetailsMapper.mapProductDetailsToProductDetailsTO(productDetailss);

        results.forEach(result -> {

            final ProductDetails productDetails = productDetailss.stream()
                    .filter(obj -> obj.getDescription().equals(result.description()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductDetailsTO(result, productDetails);
        });
    }
    
    @Test
    void mapProductDetailsTOToProductDetails() {

        final ProductDetailsTO productDetailsTO = Instancio.create(ProductDetailsTO.class);

        final ProductDetails result = this.productDetailsMapper.mapProductDetailsTOToProductDetails(productDetailsTO);

        verifyProductDetails(result, productDetailsTO);
    }

    @Test
    void mapProductDetailsTOToProductDetails_list() {

        final List<ProductDetailsTO> productDetailsTOs = Instancio.ofList(ProductDetailsTO.class)
                .size(10)
                .create();

        final List<ProductDetails> results = this.productDetailsMapper.mapProductDetailsTOToProductDetails(productDetailsTOs);

        results.forEach(result -> {

            final ProductDetailsTO productDetailsTO = productDetailsTOs.stream()
                    .filter(productDetails -> productDetails.description().equals(result.getDescription()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductDetails(result, productDetailsTO);
        });
    }

    @Test
    void mapProductDetailsTOToProductDetails_set() {

        final Set<ProductDetailsTO> productDetailsTOs = Instancio.ofSet(ProductDetailsTO.class)
                .size(10)
                .create();

        final Set<ProductDetails> results = this.productDetailsMapper.mapProductDetailsTOToProductDetails(productDetailsTOs);

        results.forEach(result -> {

            final ProductDetailsTO productDetailsTO = productDetailsTOs.stream()
                    .filter(productDetails -> productDetails.description().equals(result.getDescription()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductDetails(result, productDetailsTO);
        });
    }

    @Test
    void mapProductDetailsTOToProductDetailsPayload() {

        final ProductDetailsTO productDetailsTO = Instancio.create(ProductDetailsTO.class);

        final ProductDetailsPayload result = this.productDetailsMapper.mapProductDetailsTOToProductDetailsPayload(productDetailsTO);

        verifyProductDetailsPayload(result, productDetailsTO);
    }

    @Test
    void mapProductDetailsTOToProductDetailsPayload_list() {

        final List<ProductDetailsTO> productDetailsTOs = Instancio.ofList(ProductDetailsTO.class)
                .size(10)
                .create();

        final List<ProductDetailsPayload> results = this.productDetailsMapper.mapProductDetailsTOToProductDetailsPayload(productDetailsTOs);

        results.forEach(result -> {

            final ProductDetailsTO productDetailsTO = productDetailsTOs.stream()
                    .filter(productDetails -> productDetails.description().equals(result.getDescription()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductDetailsPayload(result, productDetailsTO);
        });
    }

    @Test
    void mapProductDetailsTOToProductDetailsPayload_set() {

        final Set<ProductDetailsTO> productDetailsTOs = Instancio.ofSet(ProductDetailsTO.class)
                .size(10)
                .create();

        final Set<ProductDetailsPayload> results = this.productDetailsMapper.mapProductDetailsTOToProductDetailsPayload(productDetailsTOs);

        results.forEach(result -> {

            final ProductDetailsTO productDetailsTO = productDetailsTOs.stream()
                    .filter(productDetails -> productDetails.description().equals(result.getDescription()))
                    .findFirst()
                    .orElseThrow();
            
            verifyProductDetailsPayload(result, productDetailsTO);
        });
    }

    @Test
    void mapProductDetailsPayloadToProductDetails() {

        final ProductDetailsPayload productDetailsPayload = Instancio.create(ProductDetailsPayload.class);

        final ProductDetails result = this.productDetailsMapper.mapProductDetailsPayloadToProductDetails(productDetailsPayload);

        verifyProductDetails(result, productDetailsPayload);
    }

    @Test
    void mapProductDetailsPayloadToProductDetails_list() {

        final List<ProductDetailsPayload> productDetailsPayloads = Instancio.ofList(ProductDetailsPayload.class)
                .size(10)
                .create();

        final List<ProductDetails> results = this.productDetailsMapper.mapProductDetailsPayloadToProductDetails(productDetailsPayloads);

        results.forEach(result -> {

            final ProductDetailsPayload productDetailsPayload = productDetailsPayloads.stream()
                    .filter(obj -> obj.getDescription().equals(result.getDescription()))
                    .findFirst()
                    .orElseThrow();

            verifyProductDetails(result, productDetailsPayload);
        });
    }

    @Test
    void mapProductDetailsPayloadToProductDetails_set() {

        final Set<ProductDetailsPayload> productDetailsPayloads = Instancio.ofSet(ProductDetailsPayload.class)
                .size(10)
                .create();

        final Set<ProductDetails> results = this.productDetailsMapper.mapProductDetailsPayloadToProductDetails(productDetailsPayloads);

        results.forEach(result -> {

            final ProductDetailsPayload productDetailsPayload = productDetailsPayloads.stream()
                    .filter(obj -> obj.getDescription().equals(result.getDescription()))
                    .findFirst()
                    .orElseThrow();

            verifyProductDetails(result, productDetailsPayload);
        });
    }

    private void verifyProductDetails(final ProductDetails result, final ProductDetailsPayload productDetailsPayload) {

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(productDetailsPayload.getDescription());
        assertThat(result.getWeight()).isEqualTo(productDetailsPayload.getWeight());
        assertThat(result.getOption()).isEqualTo(productDetailsPayload.getOption());
    }

    private void verifyProductDetailsTO(final ProductDetailsTO result, final ProductDetails productDetails) {

        assertThat(result).isNotNull();
        assertThat(result.description()).isEqualTo(productDetails.getDescription());
        assertThat(result.weight()).isEqualTo(productDetails.getWeight());
        assertThat(result.option()).isEqualTo(productDetails.getOption());
    }

    private void verifyProductDetails(final ProductDetails result, final ProductDetailsTO productDetailsTO) {

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(productDetailsTO.description());
        assertThat(result.getWeight()).isEqualTo(productDetailsTO.weight());
        assertThat(result.getOption()).isEqualTo(productDetailsTO.option());
    }

    private void verifyProductDetailsPayload(final ProductDetailsPayload result, final ProductDetailsTO productDetailsTO) {

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(productDetailsTO.description());
        assertThat(result.getWeight()).isEqualTo(productDetailsTO.weight());
        assertThat(result.getOption()).isEqualTo(productDetailsTO.option());
    }
}