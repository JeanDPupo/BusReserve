package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.PaymentMethod;
import co.edu.unimagdalena.busreserve.domine.entities.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketDtos {

    public record TicketCreateRequest(
            @NotNull Long tripId,
            @NotNull Long passengerId,
            @NotNull @Min(1) Integer seatNumber,
            @NotNull Long fromStopId,
            @NotNull Long toStopId,
            @NotNull PaymentMethod paymentMethod
            // PRICE REMOVED, debe calcularse internamente
    ) implements Serializable {}

    public record TicketUpdateRequest(
            TicketStatus status
    ) implements Serializable {}

    public record TicketResponse(
            Long id,
            Long tripId,
            LocalDate date,
            LocalDateTime departureAt,
            Long fromStop,
            Long toStop,
            Long passengerId,
            String passengerName,
            Integer seatNumber,
            BigDecimal price,
            PaymentMethod paymentMethod,
            TicketStatus status,
            String qrCode
    ) implements Serializable {}
}
