package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserCreatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserPayload;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.UserRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers.DetailsRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;

import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        GlobalRestExceptionHandler.class, UserController.class
})
class UserCreateMockMvcTest {

    private final UserRestMapper userRestMapper = Mappers.getMapper(UserRestMapper.class);
    private final DetailsRestMapper detailsMapper = Mappers.getMapper(DetailsRestMapper.class);

    @MockitoBean
    private UserService userService;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.userService);
    }

    @Test
    void usersPost() throws Exception {

        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final UserCreatePayload body = Instancio.create(UserCreatePayload.class);
        body.email("a@b.co");
        body.password("Abcdef1g");
        body.permissions(generateList(1, () -> "a"));

        when(this.userService.create(
                body.getUsername(), body.getEmail(), body.getPassword(), detailsMapper.mapDetailsPayloadToDetails(body.getDetails()), body.getRoles(), body.getPermissions()
        )).thenReturn(userEntity);

        final ResultActions resultActions = this.mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        final UserPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                UserPayload.class
        );

        verifyUser(result, userEntity);

        verify(this.userService).create(
                body.getUsername(), body.getEmail(), body.getPassword(), detailsMapper.mapDetailsPayloadToDetails(body.getDetails()), body.getRoles(), body.getPermissions()
        );
    }

    @Test
    void usersPost_validationFails() throws Exception {

        final UserCreatePayload body = Instancio.create(UserCreatePayload.class);
        body.email(null);
        body.password(null);
        body.permissions(java.util.List.of());

        this.mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usersPost_noRequestBody() throws Exception {

        this.mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    private void verifyUser(final UserPayload result, final UserEntity userEntity) {
        
        assertThat(result).isNotNull();
        final UserPayload mappedUserEntity = userRestMapper.mapUserTOToUserPayload(
                userRestMapper.mapUserEntityToUserTO(userEntity)
        );
        assertThat(result).isEqualTo(mappedUserEntity);
    }

    private static <T> java.util.List<T> generateList(final int n, final java.util.function.Supplier<T> supplier) {
        if (n <= 0) {
            return java.util.List.of();
        }
        final java.util.ArrayList<T> list = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(supplier.get());
        }
        return list;
    }
    
}