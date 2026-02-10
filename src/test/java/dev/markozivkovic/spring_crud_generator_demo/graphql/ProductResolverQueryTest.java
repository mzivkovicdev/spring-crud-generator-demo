package dev.markozivkovic.spring_crud_generator_demo.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalGraphQlExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.PageTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.ProductTO;

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
class ProductResolverQueryTest {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductBusinessService productBusinessService;

    @Autowired
    private GraphQlTester graphQlTester;

    @AfterEach
    void after() {
        verifyNoInteractions(this.productBusinessService);
        verifyNoMoreInteractions(this.productService);
    }

    @Test
    void productById() {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();

        when(productService.getById(id)).thenReturn(productModel);

        final String query = """
            query($id: ID!) {
            productById(id: $id) {
                id
            }
            }
        """;

        final ProductTO result = this.graphQlTester.document(query)
                .variable("id", id)
                .execute()
                .path("productById")
                .entity(ProductTO.class)
                .get();

        verifyProduct(result, productModel);

        verify(productService).getById(id);
    }

    @Test
    void productById_typeMismatch_error() {

        final String query = """
            query($id: Long!) {
            productById(id: $id) { id }
            }
        """;
        final String id = Instancio.create(String.class);

        this.graphQlTester.document(query)
            .variable("id", id)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void productsPage() {

        final List<ProductModel> productModels = Instancio.ofList(ProductModel.class)
                .size(10)
                .create();
        final Page<ProductModel> pageObject = new PageImpl<>(productModels);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(productService.getAll(pageNumber, pageSize)).thenReturn(pageObject);

        final String query = """
            query($pageNumber: Int!, $pageSize: Int!) {
            productsPage(pageNumber: $pageNumber, pageSize: $pageSize) {
                totalPages
                totalElements
                size
                number
                content { id }
            }
            }
        """;

        final PageTO<ProductTO> result = this.graphQlTester.document(query)
                .variable("pageNumber", pageNumber)
                .variable("pageSize", pageSize)
                .execute()
                .path("productsPage")
                .entity(new ParameterizedTypeReference<PageTO<ProductTO>>() {})
                .get();

        assertThat(result).isNotNull();
        assertThat(result.totalPages()).isGreaterThanOrEqualTo(0);
        assertThat(result.totalElements()).isGreaterThanOrEqualTo(0);
        assertThat(result.size()).isGreaterThanOrEqualTo(0);
        assertThat(result.number()).isGreaterThanOrEqualTo(0);
        assertThat(result.content()).isNotEmpty();

        result.content().forEach(item -> {

            final ProductModel src = productModels.stream()
                .filter(m -> String.valueOf(m.getId()).equals(String.valueOf(item.id())))
                .findFirst()
                .orElseThrow();

            verifyProduct(item, src);
        });

        verify(productService).getAll(pageNumber, pageSize);
    }

    @Test
    void productsPage_typeMismatch_error() {

        final String query = """
            query($pageNumber: Int!, $pageSize: Int!) {
            productsPage(pageNumber: $pageNumber, pageSize: $pageSize) { totalPages }
            }
        """;

        final String pageNumber = Instancio.create(String.class);
        final String pageSize = Instancio.create(String.class);

        this.graphQlTester.document(query)
            .variable("pageNumber", pageNumber)
            .variable("pageSize", pageSize)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void productsPage_missingPageSize() {

        final Integer pageNumber = Instancio.create(Integer.class);
        final String queryMissingPage = """
            query($pageSize: Int!) {
            productsPage(pageNumber: 0, pageSize: $pageSize) { totalPages }
            }
        """;

        this.graphQlTester.document(queryMissingPage)
            .variable("pageNumber", pageNumber)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void productsPage_missingPageNumber() {

        final Integer pageSize = Instancio.create(Integer.class);
        final String queryMissingSize = """
            query($pageNumber: Int!) {
            productsPage(pageNumber: $pageNumber, pageSize: 10) { totalPages }
            }
        """;

        this.graphQlTester.document(queryMissingSize)
            .variable("pageSize", pageSize)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }
    
    private void verifyProduct(final ProductTO result, final ProductModel src) {
        
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(src.getId());
    }

}
