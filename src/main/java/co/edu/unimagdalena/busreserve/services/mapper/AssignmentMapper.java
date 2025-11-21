package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.AssignmentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "dispatcher", ignore = true)
    @Mapping(target = "assignedAt", expression = "java(java.time.LocalDateTime.now())")
    Assignment toEntity(AssignmentCreateRequest req);

    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "driverId", source = "driver.id")
    @Mapping(target = "driverName", source = "driver.name")
    @Mapping(target = "dispatcherId", source = "dispatcher.id")
    @Mapping(target = "dispatcherName", source = "dispatcher.name")
    AssignmentResponse toResponse(Assignment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "dispatcher", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    void patch(@MappingTarget Assignment target, AssignmentUpdateRequest changes);
}
