package dev.markozivkovic.spring_crud_generator_demo.configuration;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration
public class JacksonNullExclusionConfig {

    @Bean
    JsonMapperBuilderCustomizer jsonMapperCustomizer() {
        return builder -> builder.changeDefaultPropertyInclusion(
            incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL)
        );
    }
}