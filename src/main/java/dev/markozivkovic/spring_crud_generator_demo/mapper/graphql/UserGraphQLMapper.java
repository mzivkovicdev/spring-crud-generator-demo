package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers.DetailsGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserTO;

@Mapper(uses = { DetailsGraphQLMapper.class })
public interface UserGraphQLMapper {

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

    
}