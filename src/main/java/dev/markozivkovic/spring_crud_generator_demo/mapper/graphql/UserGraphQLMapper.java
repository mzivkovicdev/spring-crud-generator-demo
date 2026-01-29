package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql;

import java.util.List;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers.DetailsGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserTO;

@Mapper(uses = { DetailsGraphQLMapper.class })
public interface UserGraphQLMapper {

    UserTO mapUserEntityToUserTO(final UserEntity model);

    List<UserTO> mapUserEntityToUserTO(final List<UserEntity> model);

    UserEntity mapUserTOToUserEntity(final UserTO transferObject);

    List<UserEntity> mapUserTOToUserEntity(final List<UserTO> transferObject);
    
}