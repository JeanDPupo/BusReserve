package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public class BaggageDtos {

    public record BaggageCreateRequest(
            @NotNull Long ticketId,
            @NotNull @Positive Double weightKg,
            // fee removed because it should be calculated server-side (recommended)
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