package dev.markozivkovic.spring_crud_generator_demo.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalGraphQlExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers.ProductDetailsGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductCreateTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductUpdateTO;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@GraphQlTest(
    controllers = ProductResolver.class,
    excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
    }
)
@AutoConfigureGraphQlTester
@Import({ GlobalGraphQlExceptionHandler.class, ResolverTestConfiguration.class })
@TestPropertySource(properties = {
    "spring.graphql.schema.locations=classpath:graphql/"
})
class ProductResolverMutationTest {
    private final ProductDetailsGraphQLMapper productDetailsMapper = Mappers.getMapper(ProductDetailsGraphQLMapper.class);

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductBusinessService productBusinessService;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private GraphQlTester graphQlTester;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.productService, this.productBusinessService);
    }

    @Test
    void createProduct() {

        final ProductModel saved = Instancio.create(ProductModel.class);
        final ProductCreateTO input = generateProductCreateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($input: ProductCreateInput!) {
              createProduct(input: $input) {
                id
              }
            }
        """;

        when(productBusinessService.create(
            input.name(), input.price(), input.usersIds(), input.uuid(), input.releaseDate(), productDetailsMapper.mapProductDetailsTOToProductDetails(input.details()), input.status()
        )).thenReturn(saved);

        final ProductTO result = this.graphQlTester.document(mutation)
            .variable("input", inputVars)
            .execute()
            .path("createProduct")
            .entity(ProductTO.class)
            .get();

        verify(this.productBusinessService).create(
            input.name(), input.price(), input.usersIds(), input.uuid(), input.releaseDate(), productDetailsMapper.mapProductDetailsTOToProductDetails(input.details()), input.status()
        );

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(saved.getId());
    }

    @Test
    void createProduct_validationFails() {
        
        final ProductCreateTO input = generateInvalidProductCreateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($input: ProductCreateInput!) {
              createProduct(input: $input) {
                id
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("input", inputVars)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void createProduct_missingInput_error() {

        final String mutation = """
            mutation {
              createProduct(input: null) {
                id
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void updateProduct() {

        final ProductModel updated = Instancio.create(ProductModel.class);
        final Long id = updated.getId();
        final ProductUpdateTO input = generateProductUpdateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($id: ID!, $input: ProductUpdateInput!) {
              updateProduct(id: $id, input: $input) {
                id
              }
            }
        """;

        when(productService.updateById(
            id,
            input.name(), input.price(), input.uuid(), input.releaseDate(), productDetailsMapper.mapProductDetailsTOToProductDetails(input.details()), input.status()
        )).thenReturn(updated);

        final ProductTO result = this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("input", inputVars)
            .execute()
            .path("updateProduct")
            .entity(ProductTO.class)
            .get();

        verify(this.productService).updateById(
                id, input.name(), input.price(), input.uuid(), input.releaseDate(), productDetailsMapper.mapProductDetailsTOToProductDetails(input.details()), input.status()
        );

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(updated.getId());
    }

    @Test
    void updateProduct_validationFails() {

        final ProductModel updated = Instancio.create(ProductModel.class);
        final Long id = updated.getId();
        final ProductUpdateTO input = generateInvalidProductUpdateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($id: ID!, $input: ProductUpdateInput!) {
              updateProduct(id: $id, input: $input) {
                id
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("input", inputVars)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void updateProduct_idTypeMismatch_error() {

        final ProductUpdateTO input = Instancio.create(ProductUpdateTO.class);
        final String id = Instancio.create(String.class);

        final String mutation = """
            mutation($id: ID!, $input: ProductUpdateInput!) {
              updateProduct(id: $id, input: $input) { id }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("input", input)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void deleteProduct() {

        final Long id = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!) { deleteProduct(id: $id) }
        """;

        final Boolean deleted = this.graphQlTester.document(mutation)
            .variable("id", id)
            .execute()
            .path("deleteProduct")
            .entity(Boolean.class)
            .get();

        verify(this.productService).deleteById(id);
        
        assertThat(deleted).isTrue();
    }

    @Test
    void deleteProduct_idTypeMismatch_error() {

        final String id = Instancio.create(String.class);
        final String mutation = """
            mutation($id: ID!) { deleteProduct(id: $id) }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", id)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void addUsersToProduct() {

        final ProductModel saved = Instancio.create(ProductModel.class);
        final Long id = saved.getId();
        final Long usersId = Instancio.create(Long.class);

        when(this.productBusinessService.addUsers(
            id, usersId
        )).thenReturn(saved);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              addUsersToProduct(id: $id, usersId: $relId) {
                id
              }
            }
        """;

        final ProductTO result = this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("relId", usersId)
            .execute()
            .path("addUsersToProduct")
            .entity(ProductTO.class)
            .get();

        verify(this.productBusinessService).addUsers(
            id, usersId
        );

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(saved.getId());
    }

    @Test
    void addUsersToProduct_idTypeMismatch_error() {

        final String id = Instancio.create(String.class);
        final Long usersId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              addUsersToProduct(id: $id, usersId: $relId) { id }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("relId", usersId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void removeUsersFromProduct() {

        final ProductModel saved = Instancio.create(ProductModel.class);
        final Long id = saved.getId();
        final Long usersId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              removeUsersFromProduct(id: $id, usersId: $relId) {
                id
              }
            }
        """;

        when(this.productBusinessService.removeUsers(id, usersId)).thenReturn(saved);

        final ProductTO result = this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("relId", usersId)
            .execute()
            .path("removeUsersFromProduct")
            .entity(ProductTO.class)
            .get();

        verify(this.productBusinessService).removeUsers(id, usersId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(saved.getId());
    }

    @Test
    void removeUsersFromProduct_idTypeMismatch_error() {

        final String id = Instancio.create(String.class);
        ;
        final Long usersId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              removeUsersFromProduct(id: $id, usersId: $relId) {
                id
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("relId", usersId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }
    @Test
    void removeUsersFromProduct_usersIdTypeMismatch_error() {

        final Long id = Instancio.create(Long.class);
        final String usersId = Instancio.create(String.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!$id: ID!) {
              removeUsersFromProduct(id: $id, usersId: $relId) {
                id
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", id)
            .variable("relId", usersId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    private static ProductCreateTO generateProductCreateTO() {
        final ProductCreateTO input = Instancio.create(ProductCreateTO.class);
        return new ProductCreateTO(
                generateString(10),
                1,
                input.usersIds(),
                input.uuid(),
                input.releaseDate(),
                input.details(),
                input.status()
        );
    }

    private static ProductCreateTO generateInvalidProductCreateTO() {
        final ProductCreateTO input = Instancio.create(ProductCreateTO.class);
        return new ProductCreateTO(
                null,
                101,
                input.usersIds(),
                input.uuid(),
                input.releaseDate(),
                input.details(),
                input.status()
        );
    }

    private static ProductUpdateTO generateProductUpdateTO() {
        final ProductUpdateTO input = Instancio.create(ProductUpdateTO.class);
        return new ProductUpdateTO(
                generateString(10),
                1,
                input.uuid(),
                input.releaseDate(),
                input.details(),
                input.status()
        );
    }

    private static ProductUpdateTO generateInvalidProductUpdateTO() {
        final ProductUpdateTO input = Instancio.create(ProductUpdateTO.class);
        return new ProductUpdateTO(
                null,
                101,
                input.uuid(),
                input.releaseDate(),
                input.details(),
                input.status()
        );
    }

    private static String generateString(final int n) {
        return Instancio.gen().string()
                .length(n)
                .get();
    }

}
