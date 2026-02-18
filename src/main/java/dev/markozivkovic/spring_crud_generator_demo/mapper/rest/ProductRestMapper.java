package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers.ProductDetailsRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.ProductTO;

@Mapper(uses = { UserRestMapper.class, ProductDetailsRestMapper.class })
public interface ProductRestMapper {

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


    ProductPayload mapProductTOToProductPayload(final ProductTO transferObject);

    List<ProductPayload> mapProductTOToProductPayload(final List<ProductTO> transferObject);

    
}