package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class StopDtos {

    public record StopCreateRequest(
            @NotNull Long routeId,
            @NotBlank String name,
            @NotNull Integer stopOrder,
            Double lat,
            Double lng
    ) implements Serializable {}

    public record StopUpdateRequest(
            String name,
            Integer stopOrder,
            Double lat,
            Double lng
    ) implements Serializable {}

    public record StopResponse(
            Long id,
            Long routeId,
            String name,
            Integer stopOrder,
            Double lat,
            Double lng
    ) implements Serializable {}
}