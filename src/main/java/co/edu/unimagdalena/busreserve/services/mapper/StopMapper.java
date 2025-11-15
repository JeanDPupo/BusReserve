package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.StopDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StopMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "orderIndex", source = "order")
    Stop toEntity(StopCreateRequest req);
    
    @Mapping(target = "routeId", source = "route.id")
    @Mapping(target = "order", source = "orderIndex")
    StopResponse toResponse(Stop entity);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "orderIndex", source = "order")
    void patch(@MappingTarget Stop target, StopUpdateRequest changes);
}
