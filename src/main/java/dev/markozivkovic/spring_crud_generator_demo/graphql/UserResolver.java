package dev.markozivkovic.spring_crud_generator_demo.graphql;

import jakarta.validation.Valid;

import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.UserGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers.DetailsGraphQLMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.PageTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserCreateTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserTO;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.UserUpdateTO;

@Controller
public class UserResolver {

    private final UserGraphQLMapper userMapper = Mappers.getMapper(UserGraphQLMapper.class);
    private final DetailsGraphQLMapper detailsMapper = Mappers.getMapper(DetailsGraphQLMapper.class);

    private final UserService userService;

    public UserResolver(final UserService userService) {
        this.userService = userService;
    }
    
    @QueryMapping
    public UserTO userById(@Argument final Long id) {
        return userMapper.mapUserEntityToUserTO(
            this.userService.getById(id)
        );
    }

    @QueryMapping
    public PageTO<UserTO> usersPage(@Argument final Integer pageNumber,
                                    @Argument final Integer pageSize) {
        
        final Page<UserEntity> pageObject = this.userService.getAll(pageNumber, pageSize);

        return new PageTO<>(
            pageObject.getTotalPages(),
            pageObject.getTotalElements(),
            pageObject.getSize(),
            pageObject.getNumber(),
            userMapper.mapUserEntityToUserTOSimple(pageObject.getContent())
        );
    }
    
    @MutationMapping
    @Validated
    public UserTO createUser(@Argument @Valid final UserCreateTO input) {
        return userMapper.mapUserEntityToUserTO(
            this.userService.create(
                input.username(), input.email(), input.password(), detailsMapper.mapDetailsTOToDetails(input.details()), input.roles(), input.permissions()
            )
        );
    }

    @MutationMapping
    @Validated
    public UserTO updateUser(@Argument final Long id, @Argument @Valid final UserUpdateTO input) {

        return userMapper.mapUserEntityToUserTO(
                this.userService.updateById(id, input.username(), input.email(), input.password(), detailsMapper.mapDetailsTOToDetails(input.details()), input.roles(), input.permissions())
        );
    }

    @MutationMapping
    public boolean deleteUser(@Argument final Long id) {
        
        this.userService.deleteById(id);
        
        return true;
    }

}