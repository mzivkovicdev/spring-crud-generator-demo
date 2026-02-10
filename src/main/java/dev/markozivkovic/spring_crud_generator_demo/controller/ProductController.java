package dev.markozivkovic.spring_crud_generator_demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.api.ProductsApi;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductCreatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductUpdatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductsGet200Response;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.UserInput;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.ProductRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController implements ProductsApi {

    private final ProductRestMapper productMapper = Mappers.getMapper(ProductRestMapper.class);

    private final ProductService productService;
    private final ProductBusinessService productBusinessService;

    public ProductController(final ProductService productService, final ProductBusinessService productBusinessService) {
        this.productService = productService;
        this.productBusinessService = productBusinessService;
    }

        @Override
        public ResponseEntity<ProductPayload> productsPost(final ProductCreatePayload body) {

        final List<Long> usersIds = (body.getUsers() != null && !body.getUsers().isEmpty()) ? 
                body.getUsers().stream()
                    .map(UserInput::getUserId)
                    .collect(Collectors.toList()) :
                List.of();
        final StatusEnum statusEnum = body.getStatus() != null ?
                StatusEnum.valueOf(body.getStatus().name()) : null;

        return ResponseEntity.ok(
            productMapper.mapProductTOToProductPayload(
                productMapper.mapProductModelToProductTO(
                    this.productBusinessService.create(
                        body.getName(), body.getPrice(), usersIds, body.getUuid(), body.getBirthDate(), statusEnum
                    )
                )
            )
        );
    
    }
    
    @Override
    public ResponseEntity<ProductPayload> productsIdGet(final Long id) {
        return ResponseEntity.ok(
            productMapper.mapProductTOToProductPayload(
                productMapper.mapProductModelToProductTO(
                    this.productService.getById(id)
                )
            )
        );
    }
    
    @Override
    public ResponseEntity<ProductsGet200Response> productsGet(final Integer pageNumber, final Integer pageSize) {

        final Page<ProductModel> pageObject = this.productService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(
            new ProductsGet200Response()
                .totalPages(pageObject.getTotalPages())
                .totalElements(pageObject.getTotalElements())
                .size(pageObject.getSize())
                .number(pageObject.getNumber())
                .content(
                    productMapper.mapProductTOToProductPayload(
                        productMapper.mapProductModelToProductTOSimple(
                            pageObject.getContent()
                        )
                    )
                )
        );
    }
    
    @Override
    public ResponseEntity<ProductPayload> productsIdPut(final Long id, final ProductUpdatePayload body) {
        return ResponseEntity.ok(
            productMapper.mapProductTOToProductPayload(
                productMapper.mapProductModelToProductTO(
                    this.productService.updateById(id, body.getName(), body.getPrice(), body.getUuid(), body.getBirthDate(), body.getStatus() != null ? StatusEnum.valueOf(body.getStatus().name()) : null)
                )
            )
        );
    }
        
    @Override
    public ResponseEntity<Void> productsIdDelete(final Long id) {

        this.productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<ProductPayload> productsIdUsersPost(final Long id,
            final UserInput body) {
        return ResponseEntity.ok(
            productMapper.mapProductTOToProductPayload(
                productMapper.mapProductModelToProductTO(
                    this.productBusinessService.addUsers(id, body.getUserId())
                )
            )
        );
    }
    
    @Override
    public ResponseEntity<Void> productsIdUsersDelete(final Long id, final Long userId) {

        this.productBusinessService.removeUsers(id, userId);
        return ResponseEntity.noContent().build();
    }

}