package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import java.util.List;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.ProductTO;

@Mapper(uses = { UserRestMapper.class })
public interface ProductRestMapper {

    ProductTO mapProductModelToProductTO(final ProductModel model);

    List<ProductTO> mapProductModelToProductTO(final List<ProductModel> model);

    ProductModel mapProductTOToProductModel(final ProductTO transferObject);

    List<ProductModel> mapProductTOToProductModel(final List<ProductTO> transferObject);

    ProductPayload mapProductTOToProductPayload(final ProductTO transferObject);

    List<ProductPayload> mapProductTOToProductPayload(final List<ProductTO> transferObject);

    
}