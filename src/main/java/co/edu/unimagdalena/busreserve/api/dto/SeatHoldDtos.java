package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class SeatHoldDtos {
    public record SeatHoldCreateRequest(
            @NotNull Long tripId,
            @NotBlank String seatNumber,
            @NotNull Long userId,
            @NotNull LocalDateTime expiresAt
    ) implements Serializable {}

    public record SeatHoldResponse(
            Long id,
            Long tripId,
            String seatNumber,
            Long userId,
            LocalDateTime expiresAt,
            HoldStatus status
    ) implements Serializable {}
}
