package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.UserTO;

class UserRestMapperTest {

    private final UserRestMapper userMapper = Mappers.getMapper(UserRestMapper.class);

    @Test
    void mapUserEntityToUserTO() {

        final UserEntity userEntity = Instancio.create(UserEntity.class);

        final UserTO result = this.userMapper.mapUserEntityToUserTO(userEntity);

        verifyUserTO(result, userEntity);
    }

    @Test
    void mapUserEntityToUserTO_list() {

        final List<UserEntity> userEntitys = Instancio.ofList(UserEntity.class)
                .size(10)
                .create();

        final List<UserTO> results = this.userMapper.mapUserEntityToUserTO(userEntitys);

        results.forEach(result -> {

            final UserEntity userEntity = userEntitys.stream()
                    .filter(obj -> obj.getUserId().equals(result.userId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyUserTO(result, userEntity);
        });
    }
    @Test
    void mapUserEntityToUserTOSimple() {

        final UserEntity userEntity = Instancio.create(UserEntity.class);

        final UserTO result = this.userMapper.mapUserEntityToUserTOSimple(userEntity);

        verifyUserTOSimple(result, userEntity);
    }

    @Test
    void mapUserEntityToUserTOSimple_list() {

        final List<UserEntity> userEntitys = Instancio.ofList(UserEntity.class)
                .size(10)
                .create();

        final List<UserTO> results = this.userMapper.mapUserEntityToUserTOSimple(userEntitys);

        results.forEach(result -> {

            final UserEntity userEntity = userEntitys.stream()
                    .filter(obj -> obj.getUserId().equals(result.userId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyUserTOSimple(result, userEntity);
        });
    }
    
    @Test
    void mapUserTOToUserEntity() {

        final UserTO userTO = Instancio.create(UserTO.class);

        final UserEntity result = this.userMapper.mapUserTOToUserEntity(userTO);

        verifyUserEntity(result, userTO);
    }

    @Test
    void mapUserTOToUserEntity_list() {

        final List<UserTO> userTOs = Instancio.ofList(UserTO.class)
                .size(10)
                .create();

        final List<UserEntity> results = this.userMapper.mapUserTOToUserEntity(userTOs);

        results.forEach(result -> {

            final UserTO userTO = userTOs.stream()
                    .filter(user -> user.userId().equals(result.getUserId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyUserEntity(result, userTO);
        });
    }

    @Test
    void mapUserTOToUserPayload() {

        final UserTO userTO = Instancio.create(UserTO.class);

        final UserPayload result = this.userMapper.mapUserTOToUserPayload(userTO);

        verifyUserPayload(result, userTO);
    }

    @Test
    void mapUserTOToUserPayload_list() {

        final List<UserTO> userTOs = Instancio.ofList(UserTO.class)
                .size(10)
                .create();

        final List<UserPayload> results = this.userMapper.mapUserTOToUserPayload(userTOs);

        results.forEach(result -> {

            final UserTO userTO = userTOs.stream()
                    .filter(user -> user.userId().equals(result.getUserId()))
                    .findFirst()
                    .orElseThrow();
            
            verifyUserPayload(result, userTO);
        });
    }

    private void verifyUserTO(final UserTO result, final UserEntity userEntity) {

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userEntity.getUserId());
        assertThat(result.username()).isEqualTo(userEntity.getUsername());
        assertThat(result.email()).isEqualTo(userEntity.getEmail());
        assertThat(result.password()).isEqualTo(userEntity.getPassword());
        assertThat(result.roles()).isEqualTo(userEntity.getRoles());
        assertThat(result.permissions()).isEqualTo(userEntity.getPermissions());
    }

    private void verifyUserTOSimple(final UserTO result, final UserEntity userEntity) {

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userEntity.getUserId());
        assertThat(result.username()).isEqualTo(userEntity.getUsername());
        assertThat(result.email()).isEqualTo(userEntity.getEmail());
        assertThat(result.password()).isEqualTo(userEntity.getPassword());
    }

    private void verifyUserEntity(final UserEntity result, final UserTO userTO) {

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userTO.userId());
        assertThat(result.getUsername()).isEqualTo(userTO.username());
        assertThat(result.getEmail()).isEqualTo(userTO.email());
        assertThat(result.getPassword()).isEqualTo(userTO.password());
        assertThat(result.getRoles()).isEqualTo(userTO.roles());
        assertThat(result.getPermissions()).isEqualTo(userTO.permissions());
    }

    private void verifyUserPayload(final UserPayload result, final UserTO userTO) {

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userTO.userId());
        assertThat(result.getUsername()).isEqualTo(userTO.username());
        assertThat(result.getEmail()).isEqualTo(userTO.email());
        assertThat(result.getPassword()).isEqualTo(userTO.password());
        assertThat(result.getRoles()).isEqualTo(userTO.roles());
        assertThat(result.getPermissions()).isEqualTo(userTO.permissions());
    }
}