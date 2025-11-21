package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.SeatHoldDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.SeatHold;
import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SeatHoldMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", constant = "HOLD")
    SeatHold toEntity(SeatHoldCreateRequest req);

    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "userId", source = "user.id")
    SeatHoldResponse toResponse(SeatHold entity);
}
