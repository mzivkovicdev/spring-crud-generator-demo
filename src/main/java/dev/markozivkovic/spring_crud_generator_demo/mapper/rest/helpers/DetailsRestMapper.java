package dev.markozivkovic.spring_crud_generator_demo.mapper.rest.helpers;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.generated.user.model.DetailsPayload;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.Details;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.helpers.DetailsTO;

@Mapper()
public interface DetailsRestMapper {

    DetailsTO mapDetailsToDetailsTO(final Details model);

    List<DetailsTO> mapDetailsToDetailsTO(final List<Details> model);

    Set<DetailsTO> mapDetailsToDetailsTO(final Set<Details> model);

    Details mapDetailsTOToDetails(final DetailsTO transferObject);

    List<Details> mapDetailsTOToDetails(final List<DetailsTO> transferObject);

    Set<Details> mapDetailsTOToDetails(final Set<DetailsTO> transferObject);


    DetailsPayload mapDetailsTOToDetailsPayload(final DetailsTO transferObject);

    List<DetailsPayload> mapDetailsTOToDetailsPayload(final List<DetailsTO> transferObject);

    Set<DetailsPayload> mapDetailsTOToDetailsPayload(final Set<DetailsTO> transferObject);


    Details mapDetailsPayloadToDetails(final DetailsPayload detailsPayload);

    List<Details> mapDetailsPayloadToDetails(final List<DetailsPayload> detailsPayload);

    Set<Details> mapDetailsPayloadToDetails(final Set<DetailsPayload> detailsPayload);
    
}