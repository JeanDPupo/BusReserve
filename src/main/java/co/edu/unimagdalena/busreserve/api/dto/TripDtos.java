package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.TripStatus;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class TripDtos {
    public record TripCreateRequest(
            @NotNull Long routeId,
            @NotNull Long busId,
            @NotNull LocalDateTime date,
            @NotNull LocalDateTime departureAt,
            @NotNull LocalDateTime arrivalEta
    ) implements Serializable {}

    public record TripUpdateRequest(
            LocalDateTime date,
            LocalDateTime departureAt,
            LocalDateTime arrivalEta,
            TripStatus status
    ) implements Serializable {}

    public record TripResponse(
            Long id,
            Long routeId,
            String routeCode,
            Long busId,
            String busPlate,
            LocalDateTime date,
            LocalDateTime departureAt,
            LocalDateTime arrivalEta,
            TripStatus status
    ) implements Serializable {}
}
