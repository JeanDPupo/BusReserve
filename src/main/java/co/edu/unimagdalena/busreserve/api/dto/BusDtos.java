package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class BusDtos {

    public record BusCreateRequest(
            @NotBlank String plate,
            @NotNull @Min(1) Integer capacity,
            List<String> amenities
    ) implements Serializable {}

    public record BusUpdateRequest(
            Integer capacity,
            List<String> amenities,
            Boolean available
    ) implements Serializable {}

    public record BusResponse(
            Long id,
            String plate,
            Integer capacity,
            List<String> amenities,
            Boolean available
    ) implements Serializable {}
}
