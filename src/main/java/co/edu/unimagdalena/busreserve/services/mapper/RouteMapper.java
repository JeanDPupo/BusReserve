package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.RouteDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stops", ignore = true)
    Route toEntity(RouteCreateRequest req);

    RouteResponse toResponse(Route entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "stops", ignore = true)
    void patch(@MappingTarget Route target, RouteUpdateRequest changes);
}