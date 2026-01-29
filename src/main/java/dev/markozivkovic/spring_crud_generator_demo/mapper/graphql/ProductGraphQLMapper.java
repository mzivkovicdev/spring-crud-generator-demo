package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import java.util.List;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductTO;

@Mapper(uses = { UserGraphQLMapper.class })
public interface ProductGraphQLMapper {

    ProductTO mapProductModelToProductTO(final ProductModel model);

    List<ProductTO> mapProductModelToProductTO(final List<ProductModel> model);

    ProductModel mapProductTOToProductModel(final ProductTO transferObject);

    List<ProductModel> mapProductTOToProductModel(final List<ProductTO> transferObject);
    
}