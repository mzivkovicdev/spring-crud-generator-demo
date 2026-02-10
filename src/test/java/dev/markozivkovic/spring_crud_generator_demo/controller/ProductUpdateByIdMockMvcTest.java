package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductUpdatePayload;
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
class ProductUpdateByIdMockMvcTest {

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
        verifyNoInteractions(this.productBusinessService);
        verifyNoMoreInteractions(this.productService);
    }

    @Test
    void productsIdPut() throws Exception {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();
        final ProductUpdatePayload body = Instancio.create(ProductUpdatePayload.class);
        body.name(generateString(1));

        when(this.productService.updateById(id, body.getName(), body.getPrice(), body.getUuid(), body.getBirthDate(), body.getStatus() != null ? StatusEnum.valueOf(body.getStatus().name()) : null)).thenReturn(productModel);

        final ResultActions resultActions = this.mockMvc.perform(put("/api/products/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        final ProductPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                ProductPayload.class
        );

        verifyProduct(result, productModel);

        verify(this.productService).updateById(id, body.getName(), body.getPrice(), body.getUuid(), body.getBirthDate(), body.getStatus() != null ? StatusEnum.valueOf(body.getStatus().name()) : null);
    }

    @Test
    void productsIdPut_invalidIdFormat() throws Exception {

        final String id = Instancio.create(String.class);
        final ProductUpdatePayload body = Instancio.create(ProductUpdatePayload.class);

        this.mockMvc.perform(put("/api/products/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void productsIdPut_validationFails() throws Exception {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();
        final ProductUpdatePayload body = Instancio.create(ProductUpdatePayload.class);
        body.name(generateString(10001));

        this.mockMvc.perform(put("/api/products/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void productsIdPut_noRequestBody() throws Exception {

        final Long id = Instancio.create(Long.class);

        this.mockMvc.perform(put("/api/products/{id}", id)
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