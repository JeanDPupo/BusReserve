package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class BaggageDtos {
    public record BaggageCreateRequest(
            @NotNull Double weightKg,
            Double fee,
            String tagCode
    ) implements Serializable {}

    public record BaggageResponse(
            Long id,
            Long ticketId,
            Double weightKg,
            Double fee,
            String tagCode
    ) implements Serializable {}
}
