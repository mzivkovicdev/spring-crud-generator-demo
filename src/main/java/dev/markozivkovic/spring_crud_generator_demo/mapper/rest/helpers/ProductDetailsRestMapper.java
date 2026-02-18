package dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductDetailsPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.ProductDetails;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.helpers.ProductDetailsTO;

@Mapper()
public interface ProductDetailsRestMapper {

    ProductDetailsTO mapProductDetailsToProductDetailsTO(final ProductDetails model);

    List<ProductDetailsTO> mapProductDetailsToProductDetailsTO(final List<ProductDetails> model);

    Set<ProductDetailsTO> mapProductDetailsToProductDetailsTO(final Set<ProductDetails> model);

    ProductDetails mapProductDetailsTOToProductDetails(final ProductDetailsTO transferObject);

    List<ProductDetails> mapProductDetailsTOToProductDetails(final List<ProductDetailsTO> transferObject);

    Set<ProductDetails> mapProductDetailsTOToProductDetails(final Set<ProductDetailsTO> transferObject);


    ProductDetailsPayload mapProductDetailsTOToProductDetailsPayload(final ProductDetailsTO transferObject);

    List<ProductDetailsPayload> mapProductDetailsTOToProductDetailsPayload(final List<ProductDetailsTO> transferObject);

    Set<ProductDetailsPayload> mapProductDetailsTOToProductDetailsPayload(final Set<ProductDetailsTO> transferObject);


    ProductDetails mapProductDetailsPayloadToProductDetails(final ProductDetailsPayload productDetailsPayload);

    List<ProductDetails> mapProductDetailsPayloadToProductDetails(final List<ProductDetailsPayload> productDetailsPayload);

    Set<ProductDetails> mapProductDetailsPayloadToProductDetails(final Set<ProductDetailsPayload> productDetailsPayload);
    
}