package dev.markozivkovic.spring_crud_generator_demo.graphql;

import jakarta.validation.Valid;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.ProductGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers.ProductDetailsGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.PageTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductCreateTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductUpdateTO;

@Controller
public class ProductResolver {

    private final ProductGraphQLMapper productMapper = Mappers.getMapper(ProductGraphQLMapper.class);
    private final ProductDetailsGraphQLMapper productDetailsMapper = Mappers.getMapper(ProductDetailsGraphQLMapper.class);

    private final ProductService productService;
    private final ProductBusinessService productBusinessService;

    public ProductResolver(final ProductService productService, final ProductBusinessService productBusinessService) {
        this.productService = productService;
        this.productBusinessService = productBusinessService;
    }
    
    @QueryMapping
    public ProductTO productById(@Argument final Long id) {
        return productMapper.mapProductModelToProductTO(
            this.productService.getById(id)
        );
    }

    @QueryMapping
    public PageTO<ProductTO> productsPage(@Argument final Integer pageNumber,
                                    @Argument final Integer pageSize) {
        
        final Page<ProductModel> pageObject = this.productService.getAll(pageNumber, pageSize);

        return new PageTO<>(
            pageObject.getTotalPages(),
            pageObject.getTotalElements(),
            pageObject.getSize(),
            pageObject.getNumber(),
            productMapper.mapProductModelToProductTOSimple(pageObject.getContent())
        );
    }
    
    @MutationMapping
    @Validated
    public ProductTO createProduct(@Argument @Valid final ProductCreateTO input) {
        return productMapper.mapProductModelToProductTO(
            this.productBusinessService.create(
                input.name(), input.price(), input.usersIds(), input.uuid(), input.releaseDate(), productDetailsMapper.mapProductDetailsTOToProductDetails(input.details()), input.status()
            )
        );
    }

    @MutationMapping
    @Validated
    public ProductTO updateProduct(@Argument final Long id, @Argument @Valid final ProductUpdateTO input) {

        return productMapper.mapProductModelToProductTO(
                this.productService.updateById(id, input.name(), input.price(), input.uuid(), input.releaseDate(), productDetailsMapper.mapProductDetailsTOToProductDetails(input.details()), input.status())
        );
    }

    @MutationMapping
    public boolean deleteProduct(@Argument final Long id) {
        
        this.productService.deleteById(id);
        
        return true;
    }

    @MutationMapping
    public ProductTO addUsersToProduct(@Argument final Long id, @Argument final Long usersId) {
        return productMapper.mapProductModelToProductTO(
            this.productBusinessService.addUsers(id, usersId)
        );
    }

    @MutationMapping
    public ProductTO removeUsersFromProduct(@Argument final Long id, @Argument final Long usersId) {

        return productMapper.mapProductModelToProductTO(
            this.productBusinessService.removeUsers(id, usersId)
        );
    }

}