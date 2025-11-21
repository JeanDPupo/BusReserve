package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.SeatType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class SeatDtos {

    public record SeatCreateRequest(
            @NotNull Long busId,
            @NotNull @Min(1) Integer number,
            @NotNull SeatType type
    ) implements Serializable {}

    public record SeatResponse(
            Long id,
            Long busId,
            Integer number,
            SeatType type
    ) implements Serializable {}
}