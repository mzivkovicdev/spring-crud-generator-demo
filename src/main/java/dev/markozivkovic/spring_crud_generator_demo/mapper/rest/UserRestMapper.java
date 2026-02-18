package dev.markozivkovic.spring_crud_generator_demo.mapper.rest;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserPayload;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers.DetailsRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.UserTO;

@Mapper(uses = { DetailsRestMapper.class })
public interface UserRestMapper {

    UserTO mapUserEntityToUserTO(final UserEntity model);

    List<UserTO> mapUserEntityToUserTO(final List<UserEntity> model);

    @Named("simple")
    @Mapping(target = "roles", source = "roles", ignore = true)
    @Mapping(target = "permissions", source = "permissions", ignore = true)
    UserTO mapUserEntityToUserTOSimple(final UserEntity model);

    @Named("simpleList")
    @IterableMapping(qualifiedByName = "simple")
    List<UserTO> mapUserEntityToUserTOSimple(final List<UserEntity> model);

    UserEntity mapUserTOToUserEntity(final UserTO transferObject);

    List<UserEntity> mapUserTOToUserEntity(final List<UserTO> transferObject);


    UserPayload mapUserTOToUserPayload(final UserTO transferObject);

    List<UserPayload> mapUserTOToUserPayload(final List<UserTO> transferObject);

    
}