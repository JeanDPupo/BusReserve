package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class RouteDtos {
    public record RouteCreateRequest(
            @NotBlank String code,
            @NotBlank String name,
            @NotBlank String origin,
            @NotBlank String destination,
            @NotNull @Min(1) Double distanceKm,
            @NotNull @Min(1) Integer durationMin
    ) implements Serializable {}

    public record RouteUpdateRequest(
            String name,
            String origin,
            String destination,
            Double distanceKm,
            Integer durationMin
    ) implements Serializable {}

    public record RouteResponse(
            Long id,
            String code,
            String name,
            String origin,
            String destination,
            Double distanceKm,
            Integer durationMin
    ) implements Serializable {}
}
