package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.TripStatus;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TripDtos {
    public record TripCreateRequest(
            @NotNull Long routeId,
            @NotNull Long busId,
            @NotNull LocalDate date,
            @NotNull LocalDateTime departureAt,
            @NotNull LocalDateTime arrivalEta
    ) implements Serializable {}

    // date removed (recommended)
    public record TripUpdateRequest(
            LocalDateTime departureAt,
            LocalDateTime arrivalEta,
            TripStatus status
    )implements Serializable {}

    public record TripResponse(
            Long id,
            Long routeId,
            String routeCode,
            String origin,
            String destination,
            Long busId,
            String busPlate,
            LocalDate date,
            LocalDateTime departureAt,
            LocalDateTime arrivalEta,
            TripStatus status
    ) implements Serializable {}
}
