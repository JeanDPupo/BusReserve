package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.BusDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "available", constant = "true")
    @Mapping(target = "trips", ignore = true)
    Bus toEntity(BusCreateRequest req);

    BusResponse toResponse(Bus entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plate", ignore = true)
    @Mapping(target = "trips", ignore = true)
    void patch(@MappingTarget Bus target, BusUpdateRequest changes);
}
