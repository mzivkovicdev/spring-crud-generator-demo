package dev.markozivkovic.spring_crud_generator_demo.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.OrderBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalGraphQlExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.OrderService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderCreateTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.OrderUpdateTO;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

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
class OrderResolverMutationTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderBusinessService orderBusinessService;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private GraphQlTester graphQlTester;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.orderService, this.orderBusinessService);
    }

    @Test
    void createOrder() {

        final OrderTable saved = Instancio.create(OrderTable.class);
        final OrderCreateTO input = generateOrderCreateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($input: OrderCreateInput!) {
              createOrder(input: $input) {
                orderId
              }
            }
        """;

        when(orderBusinessService.create(
            input.productId(), input.quantity(), input.usersIds()
        )).thenReturn(saved);

        final OrderTO result = this.graphQlTester.document(mutation)
            .variable("input", inputVars)
            .execute()
            .path("createOrder")
            .entity(OrderTO.class)
            .get();

        verify(this.orderBusinessService).create(
            input.productId(), input.quantity(), input.usersIds()
        );

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(saved.getOrderId());
    }

    @Test
    void createOrder_validationFails() {
        
        final OrderCreateTO input = generateInvalidOrderCreateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($input: OrderCreateInput!) {
              createOrder(input: $input) {
                orderId
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
    void createOrder_missingInput_error() {

        final String mutation = """
            mutation {
              createOrder(input: null) {
                orderId
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void updateOrder() {

        final OrderTable updated = Instancio.create(OrderTable.class);
        final Long orderId = updated.getOrderId();
        final OrderUpdateTO input = generateOrderUpdateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($id: ID!, $input: OrderUpdateInput!) {
              updateOrder(id: $id, input: $input) {
                orderId
              }
            }
        """;

        when(orderService.updateById(
            orderId,
            input.quantity()
        )).thenReturn(updated);

        final OrderTO result = this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("input", inputVars)
            .execute()
            .path("updateOrder")
            .entity(OrderTO.class)
            .get();

        verify(this.orderService).updateById(
                orderId, input.quantity()
        );

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(updated.getOrderId());
    }

    @Test
    void updateOrder_validationFails() {

        final OrderTable updated = Instancio.create(OrderTable.class);
        final Long orderId = updated.getOrderId();
        final OrderUpdateTO input = generateInvalidOrderUpdateTO();
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($id: ID!, $input: OrderUpdateInput!) {
              updateOrder(id: $id, input: $input) {
                orderId
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("input", inputVars)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void updateOrder_idTypeMismatch_error() {

        final OrderUpdateTO input = Instancio.create(OrderUpdateTO.class);
        final String orderId = Instancio.create(String.class);

        final String mutation = """
            mutation($id: ID!, $input: OrderUpdateInput!) {
              updateOrder(id: $id, input: $input) { orderId }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("input", input)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void deleteOrder() {

        final Long orderId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!) { deleteOrder(id: $id) }
        """;

        final Boolean deleted = this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .execute()
            .path("deleteOrder")
            .entity(Boolean.class)
            .get();

        verify(this.orderService).deleteById(orderId);
        
        assertThat(deleted).isTrue();
    }

    @Test
    void deleteOrder_idTypeMismatch_error() {

        final String orderId = Instancio.create(String.class);
        final String mutation = """
            mutation($id: ID!) { deleteOrder(id: $id) }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void addProductToOrder() {

        final OrderTable saved = Instancio.create(OrderTable.class);
        final Long orderId = saved.getOrderId();
        final Long productId = Instancio.create(Long.class);

        when(this.orderBusinessService.addProduct(
            orderId, productId
        )).thenReturn(saved);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              addProductToOrder(id: $id, productId: $relId) {
                orderId
              }
            }
        """;

        final OrderTO result = this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", productId)
            .execute()
            .path("addProductToOrder")
            .entity(OrderTO.class)
            .get();

        verify(this.orderBusinessService).addProduct(
            orderId, productId
        );

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(saved.getOrderId());
    }

    @Test
    void addProductToOrder_idTypeMismatch_error() {

        final String orderId = Instancio.create(String.class);
        final Long productId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              addProductToOrder(id: $id, productId: $relId) { orderId }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", productId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void removeProductFromOrder() {

        final OrderTable saved = Instancio.create(OrderTable.class);
        final Long orderId = saved.getOrderId();;

        final String mutation = """
            mutation($id: ID!) {
              removeProductFromOrder(id: $id) {
                orderId
              }
            }
        """;

        when(this.orderService.removeProduct(orderId)).thenReturn(saved);

        final OrderTO result = this.graphQlTester.document(mutation)
            .variable("id", orderId)
            
            .execute()
            .path("removeProductFromOrder")
            .entity(OrderTO.class)
            .get();

        verify(this.orderService).removeProduct(orderId);

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(saved.getOrderId());
    }

    @Test
    void removeProductFromOrder_orderIdTypeMismatch_error() {

        final String orderId = Instancio.create(String.class);
        ;

        final String mutation = """
            mutation($id: ID!) {
              removeProductFromOrder(id: $id) {
                orderId
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }


    @Test
    void addUsersToOrder() {

        final OrderTable saved = Instancio.create(OrderTable.class);
        final Long orderId = saved.getOrderId();
        final Long usersId = Instancio.create(Long.class);

        when(this.orderBusinessService.addUsers(
            orderId, usersId
        )).thenReturn(saved);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              addUsersToOrder(id: $id, usersId: $relId) {
                orderId
              }
            }
        """;

        final OrderTO result = this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", usersId)
            .execute()
            .path("addUsersToOrder")
            .entity(OrderTO.class)
            .get();

        verify(this.orderBusinessService).addUsers(
            orderId, usersId
        );

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(saved.getOrderId());
    }

    @Test
    void addUsersToOrder_idTypeMismatch_error() {

        final String orderId = Instancio.create(String.class);
        final Long usersId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              addUsersToOrder(id: $id, usersId: $relId) { orderId }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", usersId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void removeUsersFromOrder() {

        final OrderTable saved = Instancio.create(OrderTable.class);
        final Long orderId = saved.getOrderId();
        final Long usersId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              removeUsersFromOrder(id: $id, usersId: $relId) {
                orderId
              }
            }
        """;

        when(this.orderBusinessService.removeUsers(orderId, usersId)).thenReturn(saved);

        final OrderTO result = this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", usersId)
            .execute()
            .path("removeUsersFromOrder")
            .entity(OrderTO.class)
            .get();

        verify(this.orderBusinessService).removeUsers(orderId, usersId);

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(saved.getOrderId());
    }

    @Test
    void removeUsersFromOrder_orderIdTypeMismatch_error() {

        final String orderId = Instancio.create(String.class);
        ;
        final Long usersId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!) {
              removeUsersFromOrder(id: $id, usersId: $relId) {
                orderId
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", usersId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }
    @Test
    void removeUsersFromOrder_usersIdTypeMismatch_error() {

        final Long orderId = Instancio.create(Long.class);
        final String usersId = Instancio.create(String.class);

        final String mutation = """
            mutation($id: ID!, $relId: ID!$id: ID!) {
              removeUsersFromOrder(id: $id, usersId: $relId) {
                orderId
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", orderId)
            .variable("relId", usersId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    private static OrderCreateTO generateOrderCreateTO() {
        final OrderCreateTO input = Instancio.create(OrderCreateTO.class);
        return new OrderCreateTO(
                input.productId(),
                1,
                input.usersIds()
        );
    }

    private static OrderCreateTO generateInvalidOrderCreateTO() {
        final OrderCreateTO input = Instancio.create(OrderCreateTO.class);
        return new OrderCreateTO(
                input.productId(),
                101,
                input.usersIds()
        );
    }

    private static OrderUpdateTO generateOrderUpdateTO() {
        return new OrderUpdateTO(
                1
        );
    }

    private static OrderUpdateTO generateInvalidOrderUpdateTO() {
        return new OrderUpdateTO(
                101
        );
    }

}
