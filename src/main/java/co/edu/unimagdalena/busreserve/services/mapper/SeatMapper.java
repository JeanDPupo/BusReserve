package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.SeatDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Seat;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bus", ignore = true)
    Seat toEntity(SeatCreateRequest req);

    @Mapping(target = "busId", source = "bus.id")
    SeatResponse toResponse(Seat entity);
}
