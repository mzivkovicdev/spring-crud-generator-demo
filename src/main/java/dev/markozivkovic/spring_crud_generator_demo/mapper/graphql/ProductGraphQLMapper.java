package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductTO;

@Mapper(uses = { UserGraphQLMapper.class })
public interface ProductGraphQLMapper {

    @Mapping(target = "users", source = "users", qualifiedByName = "simple")
    ProductTO mapProductModelToProductTO(final ProductModel model);

    @Mapping(target = "users", qualifiedByName = "simple")
    List<ProductTO> mapProductModelToProductTO(final List<ProductModel> model);
    
    @Named("simple")
    @Mapping(target = "users", source = "users", ignore = true)
    ProductTO mapProductModelToProductTOSimple(final ProductModel model);

    @Named("simpleList")
    @IterableMapping(qualifiedByName = "simple")
    List<ProductTO> mapProductModelToProductTOSimple(final List<ProductModel> model);

    ProductModel mapProductTOToProductModel(final ProductTO transferObject);

    List<ProductModel> mapProductTOToProductModel(final List<ProductTO> transferObject);
    
}