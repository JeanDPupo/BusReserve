package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.TicketDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Ticket;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BaggageMapper.class})
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "fromStop", ignore = true)
    @Mapping(target = "toStop", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "status", constant = "SOLD")
    Ticket toEntity(TicketCreateRequest req);


    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "passengerId", source = "passenger.id")
    @Mapping(target = "passengerName", source = "passenger.name")
    @Mapping(target = "fromStop", source = "fromStop.id")
    @Mapping(target = "toStop", source = "toStop.id")
    TicketResponse toResponse(Ticket entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "seatNumber", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    @Mapping(target = "fromStop", ignore = true)
    @Mapping(target = "toStop", ignore = true)
    void patch(@MappingTarget Ticket target, TicketUpdateRequest changes);
}
