package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.BaggageDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BaggageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    Baggage toEntity(BaggageCreateRequest req);

    @Mapping(target = "ticketId", source = "ticket.id")
    BaggageResponse toResponse(Baggage entity);
}