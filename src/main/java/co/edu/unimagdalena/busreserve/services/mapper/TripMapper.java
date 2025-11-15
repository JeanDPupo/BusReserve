package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.TripDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TripMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "status", constant = "SCHEDULED")
    Trip toEntity(TripCreateRequest req);
    
    @Mapping(target = "routeId", source = "route.id")
    @Mapping(target = "routeCode", source = "route.code")
    @Mapping(target = "busId", source = "bus.id")
    @Mapping(target = "busPlate", source = "bus.plate")
    TripResponse toResponse(Trip entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "bus", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    void patch(@MappingTarget Trip target, TripUpdateRequest changes);
}
