package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.ProductDetails;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.helpers.ProductDetailsTO;

@Mapper()
public interface ProductDetailsGraphQLMapper {

    ProductDetailsTO mapProductDetailsToProductDetailsTO(final ProductDetails model);

    List<ProductDetailsTO> mapProductDetailsToProductDetailsTO(final List<ProductDetails> model);

    Set<ProductDetailsTO> mapProductDetailsToProductDetailsTO(final Set<ProductDetails> model);

    ProductDetails mapProductDetailsTOToProductDetails(final ProductDetailsTO transferObject);

    List<ProductDetails> mapProductDetailsTOToProductDetails(final List<ProductDetailsTO> transferObject);

    Set<ProductDetails> mapProductDetailsTOToProductDetails(final Set<ProductDetailsTO> transferObject);

    
}