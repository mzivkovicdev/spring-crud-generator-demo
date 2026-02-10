package dev.markozivkovic.spring_crud_generator_demo.controller;


import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.markozivkovic.spring_crud_generator_demo.generated.user.api.UsersApi;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserCreatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UserUpdatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.UsersGet200Response;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.UserRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers.DetailsRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController implements UsersApi {

    private final UserRestMapper userMapper = Mappers.getMapper(UserRestMapper.class);
    private final DetailsRestMapper detailsMapper = Mappers.getMapper(DetailsRestMapper.class);

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

        @Override
        public ResponseEntity<UserPayload> usersPost(final UserCreatePayload body) {


        return ResponseEntity.ok(
            userMapper.mapUserTOToUserPayload(
                userMapper.mapUserEntityToUserTO(
                    this.userService.create(
                        body.getUsername(), body.getEmail(), body.getPassword(), detailsMapper.mapDetailsPayloadToDetails(body.getDetails()), body.getRoles(), body.getPermissions()
                    )
                )
            )
        );
    
    }
    
    @Override
    public ResponseEntity<UserPayload> usersIdGet(final Long id) {
        return ResponseEntity.ok(
            userMapper.mapUserTOToUserPayload(
                userMapper.mapUserEntityToUserTO(
                    this.userService.getById(id)
                )
            )
        );
    }
    
    @Override
    public ResponseEntity<UsersGet200Response> usersGet(final Integer pageNumber, final Integer pageSize) {

        final Page<UserEntity> pageObject = this.userService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok().body(
            new UsersGet200Response()
                .totalPages(pageObject.getTotalPages())
                .totalElements(pageObject.getTotalElements())
                .size(pageObject.getSize())
                .number(pageObject.getNumber())
                .content(
                    userMapper.mapUserTOToUserPayload(
                        userMapper.mapUserEntityToUserTOSimple(
                            pageObject.getContent()
                        )
                    )
                )
        );
    }
    
    @Override
    public ResponseEntity<UserPayload> usersIdPut(final Long id, final UserUpdatePayload body) {
        return ResponseEntity.ok(
            userMapper.mapUserTOToUserPayload(
                userMapper.mapUserEntityToUserTO(
                    this.userService.updateById(id, body.getUsername(), body.getEmail(), body.getPassword(), detailsMapper.mapDetailsPayloadToDetails(body.getDetails()), body.getRoles(), body.getPermissions())
                )
            )
        );
    }
        
    @Override
    public ResponseEntity<Void> usersIdDelete(final Long id) {

        this.userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    
}