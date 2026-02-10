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

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalGraphQlExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.PageTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderTO;

@GraphQlTest(
    controllers = OrderResolver.class,
    excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
    }
)
@AutoConfigureGraphQlTester
@Import({ GlobalGraphQlExceptionHandler.class, ResolverTestConfiguration.class })
@TestPropertySource(properties = {
    "spring.graphql.schema.locations=classpath:graphql/"
})
class OrderResolverQueryTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderBusinessService orderBusinessService;

    @Autowired
    private GraphQlTester graphQlTester;

    @AfterEach
    void after() {
        verifyNoInteractions(this.orderBusinessService);
        verifyNoMoreInteractions(this.orderService);
    }

    @Test
    void orderById() {

        final OrderTable orderTable = Instancio.create(OrderTable.class);
        final Long orderId = orderTable.getOrderId();

        when(orderService.getById(orderId)).thenReturn(orderTable);

        final String query = """
            query($id: ID!) {
            orderById(id: $id) {
                orderId
            }
            }
        """;

        final OrderTO result = this.graphQlTester.document(query)
                .variable("id", orderId)
                .execute()
                .path("orderById")
                .entity(OrderTO.class)
                .get();

        verifyOrder(result, orderTable);

        verify(orderService).getById(orderId);
    }

    @Test
    void orderById_typeMismatch_error() {

        final String query = """
            query($id: Long!) {
            orderById(id: $id) { orderId }
            }
        """;
        final String orderId = Instancio.create(String.class);

        this.graphQlTester.document(query)
            .variable("id", orderId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void ordersPage() {

        final List<OrderTable> orderTables = Instancio.ofList(OrderTable.class)
                .size(10)
                .create();
        final Page<OrderTable> pageObject = new PageImpl<>(orderTables);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(orderService.getAll(pageNumber, pageSize)).thenReturn(pageObject);

        final String query = """
            query($pageNumber: Int!, $pageSize: Int!) {
            ordersPage(pageNumber: $pageNumber, pageSize: $pageSize) {
                totalPages
                totalElements
                size
                number
                content { orderId }
            }
            }
        """;

        final PageTO<OrderTO> result = this.graphQlTester.document(query)
                .variable("pageNumber", pageNumber)
                .variable("pageSize", pageSize)
                .execute()
                .path("ordersPage")
                .entity(new ParameterizedTypeReference<PageTO<OrderTO>>() {})
                .get();

        assertThat(result).isNotNull();
        assertThat(result.totalPages()).isGreaterThanOrEqualTo(0);
        assertThat(result.totalElements()).isGreaterThanOrEqualTo(0);
        assertThat(result.size()).isGreaterThanOrEqualTo(0);
        assertThat(result.number()).isGreaterThanOrEqualTo(0);
        assertThat(result.content()).isNotEmpty();

        result.content().forEach(item -> {

            final OrderTable src = orderTables.stream()
                .filter(m -> String.valueOf(m.getOrderId()).equals(String.valueOf(item.orderId())))
                .findFirst()
                .orElseThrow();

            verifyOrder(item, src);
        });

        verify(orderService).getAll(pageNumber, pageSize);
    }

    @Test
    void ordersPage_typeMismatch_error() {

        final String query = """
            query($pageNumber: Int!, $pageSize: Int!) {
            ordersPage(pageNumber: $pageNumber, pageSize: $pageSize) { totalPages }
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
    void ordersPage_missingPageSize() {

        final Integer pageNumber = Instancio.create(Integer.class);
        final String queryMissingPage = """
            query($pageSize: Int!) {
            ordersPage(pageNumber: 0, pageSize: $pageSize) { totalPages }
            }
        """;

        this.graphQlTester.document(queryMissingPage)
            .variable("pageNumber", pageNumber)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void ordersPage_missingPageNumber() {

        final Integer pageSize = Instancio.create(Integer.class);
        final String queryMissingSize = """
            query($pageNumber: Int!) {
            ordersPage(pageNumber: $pageNumber, pageSize: 10) { totalPages }
            }
        """;

        this.graphQlTester.document(queryMissingSize)
            .variable("pageSize", pageSize)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }
    
    private void verifyOrder(final OrderTO result, final OrderTable src) {
        
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(src.getOrderId());
    }

}
