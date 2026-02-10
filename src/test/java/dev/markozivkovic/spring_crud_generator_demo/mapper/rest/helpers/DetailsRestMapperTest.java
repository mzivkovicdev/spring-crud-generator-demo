package dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.DetailsPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.Details;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.helpers.DetailsTO;

class DetailsRestMapperTest {

    private final DetailsRestMapper detailsMapper = Mappers.getMapper(DetailsRestMapper.class);

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

    @Test
    void mapDetailsTOToDetailsPayload() {

        final DetailsTO detailsTO = Instancio.create(DetailsTO.class);

        final DetailsPayload result = this.detailsMapper.mapDetailsTOToDetailsPayload(detailsTO);

        verifyDetailsPayload(result, detailsTO);
    }

    @Test
    void mapDetailsTOToDetailsPayload_list() {

        final List<DetailsTO> detailsTOs = Instancio.ofList(DetailsTO.class)
                .size(10)
                .create();

        final List<DetailsPayload> results = this.detailsMapper.mapDetailsTOToDetailsPayload(detailsTOs);

        results.forEach(result -> {

            final DetailsTO detailsTO = detailsTOs.stream()
                    .filter(details -> details.firstName().equals(result.getFirstName()))
                    .findFirst()
                    .orElseThrow();
            
            verifyDetailsPayload(result, detailsTO);
        });
    }
    @Test
    void mapDetailsPayloadToDetails() {

        final DetailsPayload detailsPayload = Instancio.create(DetailsPayload.class);

        final Details result = this.detailsMapper.mapDetailsPayloadToDetails(detailsPayload);

        verifyDetails(result, detailsPayload);
    }

    @Test
    void mapDetailsPayloadToDetails_list() {

        final List<DetailsPayload> detailsPayloads = Instancio.ofList(DetailsPayload.class)
                .size(10)
                .create();

        final List<Details> results = this.detailsMapper.mapDetailsPayloadToDetails(detailsPayloads);

        results.forEach(result -> {

            final DetailsPayload detailsPayload = detailsPayloads.stream()
                    .filter(obj -> obj.getFirstName().equals(result.getFirstName()))
                    .findFirst()
                    .orElseThrow();

            verifyDetails(result, detailsPayload);
        });
    }

    private void verifyDetails(final Details result, final DetailsPayload detailsPayload) {

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(detailsPayload.getFirstName());
        assertThat(result.getLastName()).isEqualTo(detailsPayload.getLastName());
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

    private void verifyDetailsPayload(final DetailsPayload result, final DetailsTO detailsTO) {

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(detailsTO.firstName());
        assertThat(result.getLastName()).isEqualTo(detailsTO.lastName());
    }
}