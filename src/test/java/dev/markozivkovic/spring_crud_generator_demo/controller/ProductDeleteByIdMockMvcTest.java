package dev.markozivkovic.spring_crud_generator_demo.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.markozivkovic.spring_crud_generator_demo.businessservice.ProductBusinessService;
import dev.markozivkovic.spring_crud_generator_demo.exception.handlers.GlobalRestExceptionHandler;
import dev.markozivkovic.spring_crud_generator_demo.persistance.service.ProductService;


@WebMvcTest(excludeAutoConfiguration = {
        OAuth2ClientAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        GlobalRestExceptionHandler.class, ProductController.class
})
class ProductDeleteByIdMockMvcTest {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductBusinessService productBusinessService;
    
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void after() {
        verifyNoInteractions(this.productBusinessService);
        verifyNoMoreInteractions(this.productService);
    }

    @Test
    void productsIdDelete() throws Exception {

        final Long id = Instancio.create(Long.class);

        this.mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());

        verify(this.productService).deleteById(id);
    }

    @Test
    void productsIdDelete_invalidIdFormat() throws Exception {

        final String id = Instancio.create(String.class);

        this.mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isBadRequest());
    }
}