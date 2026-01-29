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

import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalGraphQlExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers.DetailsGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserCreateTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserUpdateTO;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@GraphQlTest(
    controllers = UserResolver.class,
    excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
    }
)
@AutoConfigureGraphQlTester
@Import({ GlobalGraphQlExceptionHandler.class, ResolverTestConfiguration.class })
@TestPropertySource(properties = {
    "spring.graphql.schema.locations=classpath:graphql/"
})
class UserResolverMutationTest {
    private final DetailsGraphQLMapper detailsMapper = Mappers.getMapper(DetailsGraphQLMapper.class);

    @MockitoBean
    private UserService userService;


    @Autowired
    private JsonMapper mapper;

    @Autowired
    private GraphQlTester graphQlTester;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.userService);
    }

    @Test
    void createUser() {

        final UserEntity saved = Instancio.create(UserEntity.class);
        final UserCreateTO input = Instancio.create(UserCreateTO.class);
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($input: UserCreateInput!) {
              createUser(input: $input) {
                userId
              }
            }
        """;

        when(userService.create(
            input.username(), input.email(), input.password(), detailsMapper.mapDetailsTOToDetails(input.details()), input.roles(), input.permissions()
        )).thenReturn(saved);

        final UserTO result = this.graphQlTester.document(mutation)
            .variable("input", inputVars)
            .execute()
            .path("createUser")
            .entity(UserTO.class)
            .get();

        verify(this.userService).create(
            input.username(), input.email(), input.password(), detailsMapper.mapDetailsTOToDetails(input.details()), input.roles(), input.permissions()
        );

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(saved.getUserId());
    }

    @Test
    void createUser_missingInput_error() {

        final String mutation = """
            mutation {
              createUser(input: null) {
                userId
              }
            }
        """;

        this.graphQlTester.document(mutation)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void updateUser() {

        final UserEntity updated = Instancio.create(UserEntity.class);
        final Long userId = updated.getUserId();
        final UserUpdateTO input = Instancio.create(UserUpdateTO.class);
        final Map<String, Object> inputVars = this.mapper.convertValue(
                input, new TypeReference<Map<String,Object>>() {}
        );

        final String mutation = """
            mutation($id: ID!, $input: UserUpdateInput!) {
              updateUser(id: $id, input: $input) {
                userId
              }
            }
        """;

        when(userService.updateById(
            userId,
            input.username(), input.email(), input.password(), detailsMapper.mapDetailsTOToDetails(input.details()), input.roles(), input.permissions()
        )).thenReturn(updated);

        final UserTO result = this.graphQlTester.document(mutation)
            .variable("id", userId)
            .variable("input", inputVars)
            .execute()
            .path("updateUser")
            .entity(UserTO.class)
            .get();

        verify(this.userService).updateById(
                userId, input.username(), input.email(), input.password(), detailsMapper.mapDetailsTOToDetails(input.details()), input.roles(), input.permissions()
        );

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(updated.getUserId());
    }

    @Test
    void updateUser_idTypeMismatch_error() {

        final UserUpdateTO input = Instancio.create(UserUpdateTO.class);
        final String userId = Instancio.create(String.class);

        final String mutation = """
            mutation($id: ID!, $input: UserUpdateInput!) {
              updateUser(id: $id, input: $input) { userId }
            }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", userId)
            .variable("input", input)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void deleteUser() {

        final Long userId = Instancio.create(Long.class);

        final String mutation = """
            mutation($id: ID!) { deleteUser(id: $id) }
        """;

        final Boolean deleted = this.graphQlTester.document(mutation)
            .variable("id", userId)
            .execute()
            .path("deleteUser")
            .entity(Boolean.class)
            .get();

        verify(this.userService).deleteById(userId);
        
        assertThat(deleted).isTrue();
    }

    @Test
    void deleteUser_idTypeMismatch_error() {

        final String userId = Instancio.create(String.class);
        final String mutation = """
            mutation($id: ID!) { deleteUser(id: $id) }
        """;

        this.graphQlTester.document(mutation)
            .variable("id", userId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }
}
