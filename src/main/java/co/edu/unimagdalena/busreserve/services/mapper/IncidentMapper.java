package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.IncidentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Incident;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Incident toEntity(IncidentCreateRequest req);

    IncidentResponse toResponse(Incident entity);
}