package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductCreatePayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.UserInput;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.ProductRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;

import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        GlobalRestExceptionHandler.class, ProductController.class
})
class ProductCreateMockMvcTest {

    private final ProductRestMapper productRestMapper = Mappers.getMapper(ProductRestMapper.class);

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductBusinessService productBusinessService;
    
    @Autowired
    private JsonMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.productBusinessService);
        verifyNoInteractions(this.productService);
    }

    @Test
    void productsPost() throws Exception {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final ProductCreatePayload body = Instancio.create(ProductCreatePayload.class);
        body.name(generateString(1));
        final List<Long> usersIds = (body.getUsers() != null && !body.getUsers().isEmpty()) ? 
                body.getUsers().stream()
                    .map(UserInput::getUserId)
                    .toList() : 
                List.of();
        final StatusEnum statusEnum = body.getStatus() != null ?
                StatusEnum.valueOf(body.getStatus().name()) : null;

        when(this.productBusinessService.create(
                body.getName(), body.getPrice(), usersIds, body.getUuid(), body.getBirthDate(), statusEnum
        )).thenReturn(productModel);

        final ResultActions resultActions = this.mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        final ProductPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                ProductPayload.class
        );

        verifyProduct(result, productModel);

        verify(this.productBusinessService).create(
                body.getName(), body.getPrice(), usersIds, body.getUuid(), body.getBirthDate(), statusEnum
        );
    }

    @Test
    void productsPost_validationFails() throws Exception {

        final ProductCreatePayload body = Instancio.create(ProductCreatePayload.class);
        body.name(generateString(10001));

        this.mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void productsPost_noRequestBody() throws Exception {

        this.mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    private void verifyProduct(final ProductPayload result, final ProductModel productModel) {
        
        assertThat(result).isNotNull();
        final ProductPayload mappedProductModel = productRestMapper.mapProductTOToProductPayload(
                productRestMapper.mapProductModelToProductTO(productModel)
        );
        assertThat(result).isEqualTo(mappedProductModel);
    }

    private static String generateString(final int n) {
        return Instancio.gen().string()
                .length(n)
                .get();
    }
}