package dev.markozivkovic.spring_crud_generator_demo.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
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

import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalGraphQlExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.PageTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserTO;

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
class UserResolverQueryTest {

    @MockitoBean
    private UserService userService;


    @Autowired
    private GraphQlTester graphQlTester;

    @AfterEach
    void after() {
        
        verifyNoMoreInteractions(this.userService);
    }

    @Test
    void userById() {

        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(userService.getById(userId)).thenReturn(userEntity);

        final String query = """
            query($id: ID!) {
            userById(id: $id) {
                userId
            }
            }
        """;

        final UserTO result = this.graphQlTester.document(query)
                .variable("id", userId)
                .execute()
                .path("userById")
                .entity(UserTO.class)
                .get();

        verifyUser(result, userEntity);

        verify(userService).getById(userId);
    }

    @Test
    void userById_typeMismatch_error() {

        final String query = """
            query($id: Long!) {
            userById(id: $id) { userId }
            }
        """;
        final String userId = Instancio.create(String.class);

        this.graphQlTester.document(query)
            .variable("id", userId)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void usersPage() {

        final List<UserEntity> userEntitys = Instancio.ofList(UserEntity.class)
                .size(10)
                .create();
        final Page<UserEntity> pageObject = new PageImpl<>(userEntitys);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(userService.getAll(pageNumber, pageSize)).thenReturn(pageObject);

        final String query = """
            query($pageNumber: Int!, $pageSize: Int!) {
            usersPage(pageNumber: $pageNumber, pageSize: $pageSize) {
                totalPages
                totalElements
                size
                number
                content { userId }
            }
            }
        """;

        final PageTO<UserTO> result = this.graphQlTester.document(query)
                .variable("pageNumber", pageNumber)
                .variable("pageSize", pageSize)
                .execute()
                .path("usersPage")
                .entity(new ParameterizedTypeReference<PageTO<UserTO>>() {})
                .get();

        assertThat(result).isNotNull();
        assertThat(result.totalPages()).isGreaterThanOrEqualTo(0);
        assertThat(result.totalElements()).isGreaterThanOrEqualTo(0);
        assertThat(result.size()).isGreaterThanOrEqualTo(0);
        assertThat(result.number()).isGreaterThanOrEqualTo(0);
        assertThat(result.content()).isNotEmpty();

        result.content().forEach(item -> {

            final UserEntity src = userEntitys.stream()
                .filter(m -> String.valueOf(m.getUserId()).equals(String.valueOf(item.userId())))
                .findFirst()
                .orElseThrow();

            verifyUser(item, src);
        });

        verify(userService).getAll(pageNumber, pageSize);
    }

    @Test
    void usersPage_typeMismatch_error() {

        final String query = """
            query($pageNumber: Int!, $pageSize: Int!) {
            usersPage(pageNumber: $pageNumber, pageSize: $pageSize) { totalPages }
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
    void usersPage_missingPageSize() {

        final Integer pageNumber = Instancio.create(Integer.class);
        final String queryMissingPage = """
            query($pageSize: Int!) {
            usersPage(pageNumber: 0, pageSize: $pageSize) { totalPages }
            }
        """;

        this.graphQlTester.document(queryMissingPage)
            .variable("pageNumber", pageNumber)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }

    @Test
    void usersPage_missingPageNumber() {

        final Integer pageSize = Instancio.create(Integer.class);
        final String queryMissingSize = """
            query($pageNumber: Int!) {
            usersPage(pageNumber: $pageNumber, pageSize: 10) { totalPages }
            }
        """;

        this.graphQlTester.document(queryMissingSize)
            .variable("pageSize", pageSize)
            .execute()
            .errors()
            .satisfy(errors -> assertThat(errors).isNotEmpty());
    }
    
    private void verifyUser(final UserTO result, final UserEntity src) {
        
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(src.getUserId());
    }

}
