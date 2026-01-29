package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.Details;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.helpers.DetailsTO;

class DetailsGraphQlMapperTest {

    private final DetailsGraphQLMapper detailsMapper = Mappers.getMapper(DetailsGraphQLMapper.class);

    @Test
    void mapDetailsToDetailsTO() {

        final Details details = Instancio.create(Details.class);

        final DetailsTO result = this.detailsMapper.mapDetailsToDetailsTO(details);

        verifyDetailsTO(result, details);
    }

    @Test
    void mapDetailsToDetailsTO_list() {

        final List<Details> detailss = Instancio.ofList(Details.class)
                .size(10)
                .create();

        final List<DetailsTO> results = this.detailsMapper.mapDetailsToDetailsTO(detailss);

        results.forEach(result -> {

            final Details details = detailss.stream()
                    .filter(obj -> obj.getFirstName().equals(result.firstName()))
                    .findFirst()
                    .orElseThrow();
            
            verifyDetailsTO(result, details);
        });
    }

    @Test
    void mapDetailsTOToDetails() {

        final DetailsTO detailsTO = Instancio.create(DetailsTO.class);

        final Details result = this.detailsMapper.mapDetailsTOToDetails(detailsTO);

        verifyDetails(result, detailsTO);
    }

    @Test
    void mapDetailsTOToDetails_list() {

        final List<DetailsTO> detailsTOs = Instancio.ofList(DetailsTO.class)
                .size(10)
                .create();

        final List<Details> results = this.detailsMapper.mapDetailsTOToDetails(detailsTOs);

        results.forEach(result -> {

            final DetailsTO detailsTO = detailsTOs.stream()
                    .filter(details -> details.firstName().equals(result.getFirstName()))
                    .findFirst()
                    .orElseThrow();
            
            verifyDetails(result, detailsTO);
        });
    }


    private void verifyDetailsTO(final DetailsTO result, final Details details) {

        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo(details.getFirstName());
        assertThat(result.lastName()).isEqualTo(details.getLastName());
    }

    private void verifyDetails(final Details result, final DetailsTO detailsTO) {

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(detailsTO.firstName());
        assertThat(result.getLastName()).isEqualTo(detailsTO.lastName());
    }

}