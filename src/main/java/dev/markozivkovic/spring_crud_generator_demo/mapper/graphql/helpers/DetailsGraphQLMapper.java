package dev.markozivkovic.spring_crud_generator_demo.mapper.graphql.helpers;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.Details;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.helpers.DetailsTO;

@Mapper()
public interface DetailsGraphQLMapper {

    DetailsTO mapDetailsToDetailsTO(final Details model);

    List<DetailsTO> mapDetailsToDetailsTO(final List<Details> model);

    Set<DetailsTO> mapDetailsToDetailsTO(final Set<Details> model);

    Details mapDetailsTOToDetails(final DetailsTO transferObject);

    List<Details> mapDetailsTOToDetails(final List<DetailsTO> transferObject);

    Set<Details> mapDetailsTOToDetails(final Set<DetailsTO> transferObject);

    
}