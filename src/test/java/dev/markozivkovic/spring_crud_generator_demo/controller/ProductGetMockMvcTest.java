package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductsGet200Response;
import dev.markozivkovic.spring_crud_generator_demo.mapper.rest.ProductRestMapper;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        GlobalRestExceptionHandler.class, ProductController.class
})
class ProductGetMockMvcTest {

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
    void productsIdGet() throws Exception {

        final ProductModel productModel = Instancio.create(ProductModel.class);
        final Long id = productModel.getId();

        when(this.productService.getById(id)).thenReturn(productModel);

        final ResultActions resultActions = this.mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk());

        final ProductPayload result = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                ProductPayload.class
        );

        verifyProduct(result, productModel);

        verify(this.productService).getById(id);
    }

    @Test
    void productsIdGet_invalidIdFormat() throws Exception {

        final String id = Instancio.create(String.class);

        this.mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void productsGet() throws Exception {
        final List<ProductModel> productModels = Instancio.ofList(ProductModel.class)
                        .size(10)
                        .create();
        final Page<ProductModel> pageProductModels = new PageImpl<>(productModels);
        final Integer pageNumber = Instancio.create(Integer.class);
        final Integer pageSize = Instancio.create(Integer.class);

        when(this.productService.getAll(pageNumber, pageSize)).thenReturn(pageProductModels);

        final ResultActions resultActions = this.mockMvc.perform(get("/api/products")
                                .queryParam("pageNumber", String.format("%s", pageNumber))
                                .queryParam("pageSize", String.format("%s", pageSize)))
                        .andExpect(status().isOk());

        final ProductsGet200Response results = this.mapper.readValue(
                resultActions.andReturn().getResponse().getContentAsString(),
                ProductsGet200Response.class
        );

        assertThat(results).isNotNull();
        assertThat(results.getTotalPages()).isNotNegative();
        assertThat(results.getTotalElements()).isNotNegative();
        assertThat(results.getSize()).isNotNegative();
        assertThat(results.getNumber()).isNotNegative();
        assertThat(results.getContent()).isNotEmpty();

        results.getContent().forEach(result -> {

            final ProductModel productModel = productModels.stream()
                    .filter(obj -> obj.getId().toString().equals(result.getId().toString()))
                    .findFirst()
                    .orElseThrow();

            verifyProduct(result, productModel);
        });

        verify(this.productService).getAll(pageNumber, pageSize);
    }

    @Test
    void productsGet_missingPageNumberParameter() throws Exception {

        final Integer pageSize = Instancio.create(Integer.class);

        this.mockMvc.perform(get("/api/products")
                                .queryParam("pageSize", String.format("%s", pageSize)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void productsGet_missingPageSizeParameter() throws Exception {

        final Integer pageNumber = Instancio.create(Integer.class);

        this.mockMvc.perform(get("/api/products")
                                .queryParam("pageNumber", String.format("%s", pageNumber)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void productsGet_typeMissmatch() throws Exception {

        final String pageNumber = Instancio.create(String.class);
        final String pageSize = Instancio.create(String.class);

        this.mockMvc.perform(get("/api/products")
                                .queryParam("pageSize", String.format("%s", pageSize))
                                .queryParam("pageNumber", String.format("%s", pageNumber)))
                        .andExpect(status().isBadRequest());
    }

    private void verifyProduct(final ProductPayload result, final ProductModel productModel) {
        
        assertThat(result).isNotNull();
        final ProductPayload mappedProductModel = productRestMapper.mapProductTOToProductPayload(
                productRestMapper.mapProductModelToProductTO(productModel)
        );
        assertThat(result).isEqualTo(mappedProductModel);
    }

}