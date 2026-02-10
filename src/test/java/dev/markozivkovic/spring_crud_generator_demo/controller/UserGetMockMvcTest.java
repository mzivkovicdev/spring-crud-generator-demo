package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UsersGet200Response;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.UserRestMapper;
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
class UserGetMockMvcTest {

    private final UserRestMapper userRestMapper = Mappers.getMapper(UserRestMapper.class);

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
    void usersIdGet() throws Exception {

        final UserEntity userEntity = Instancio.create(UserEntity.class);
        final Long userId = userEntity.getUserId();

        when(this.userService.getById(userId)).thenReturn(userEntity);

        final ResultActions resultActions = this.mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk());

        final UserPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                UserPayload.class
        );

        verifyUser(result, userEntity);

        verify(this.userService).getById(userId);
    }

    @Test
    void usersIdGet_invalidUserIdFormat() throws Exception {

        final String userId = Instancio.create(String.class);

        this.mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usersGet() throws Exception {
        final List<UserEntity> userEntitys = Instancio.ofList(UserEntity.class)
                        .size(10)
                        .create();
        final Page<UserEntity> pageUserEntitys = new PageImpl<>(userEntitys);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(this.userService.getAll(pageNumber, pageSize)).thenReturn(pageUserEntitys);

        final ResultActions resultActions = this.mockMvc.perform(get("/api/users")
                                .queryParam("pageNumber", String.format("%s", pageNumber))
                                .queryParam("pageSize", String.format("%s", pageSize)))
                        .andExpect(status().isOk());

        final UsersGet200Response results = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                UsersGet200Response.class
        );

        assertThat(results).isNotNull();
        assertThat(results.getTotalPages()).isNotNegative();
        assertThat(results.getTotalElements()).isNotNegative();
        assertThat(results.getSize()).isNotNegative();
        assertThat(results.getNumber()).isNotNegative();
        assertThat(results.getContent()).isNotEmpty();

        results.getContent().forEach(result -> {

            final UserEntity userEntity = userEntitys.stream()
                    .filter(obj -> obj.getUserId().toString().equals(result.getUserId().toString()))
                    .findFirst()
                    .orElseThrow();

            verifyUserSimple(result, userEntity);
        });

        verify(this.userService).getAll(pageNumber, pageSize);
    }

    @Test
    void usersGet_missingPageNumberParameter() throws Exception {

        final Integer pageSize = Instancio.create(Integer.class);

        this.mockMvc.perform(get("/api/users")
                                .queryParam("pageSize", String.format("%s", pageSize)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void usersGet_missingPageSizeParameter() throws Exception {

        final Integer pageNumber = Instancio.create(Integer.class);

        this.mockMvc.perform(get("/api/users")
                                .queryParam("pageNumber", String.format("%s", pageNumber)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void usersGet_typeMissmatch() throws Exception {

        final String pageNumber = Instancio.create(String.class);
        final String pageSize = Instancio.create(String.class);

        this.mockMvc.perform(get("/api/users")
                                .queryParam("pageSize", String.format("%s", pageSize))
                                .queryParam("pageNumber", String.format("%s", pageNumber)))
                        .andExpect(status().isBadRequest());
    }

    private void verifyUser(final UserPayload result, final UserEntity userEntity) {
        
        assertThat(result).isNotNull();
        final UserPayload mappedUserEntity = userRestMapper.mapUserTOToUserPayload(
                userRestMapper.mapUserEntityToUserTO(userEntity)
        );
        assertThat(result).isEqualTo(mappedUserEntity);
    }

    private void verifyUserSimple(final UserPayload result, final UserEntity userEntity) {
        
        assertThat(result).isNotNull();
        final UserPayload mappedUserEntity = userRestMapper.mapUserTOToUserPayload(
                userRestMapper.mapUserEntityToUserTOSimple(userEntity)
        );
        assertThat(result).isEqualTo(mappedUserEntity);
    }
}