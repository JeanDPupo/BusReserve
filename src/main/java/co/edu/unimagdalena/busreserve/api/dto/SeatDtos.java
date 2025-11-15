package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class SeatDtos {
    public record SeatCreateRequest(
            @NotNull Long busId,
            @NotBlank String number,
            @NotNull SeatType type
    ) implements Serializable {}

    public record SeatResponse(
            Long id,
            Long busId,
            String number,
            SeatType type
    ) implements Serializable {}
}
