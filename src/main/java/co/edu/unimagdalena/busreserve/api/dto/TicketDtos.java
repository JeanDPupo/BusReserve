package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.PaymentMethod;
import co.edu.unimagdalena.busreserve.domine.entities.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class TicketDtos {
    public record TicketCreateRequest(
            @NotNull Long tripId,
            @NotNull Long passengerId,
            @NotBlank String seatNumber,
            @NotNull Long fromStopId,
            @NotNull Long toStopId,
            @NotNull Double price,
            @NotNull PaymentMethod paymentMethod
    ) implements Serializable {}

    public record TicketUpdateRequest(
            TicketStatus status
    ) implements Serializable {}

    public record TicketResponse(
            Long id,
            Long tripId,
            Long passengerId,
            String passengerName,
            String seatNumber,
            Long fromStopId,
            Long toStopId,
            Double price,
            PaymentMethod paymentMethod,
            TicketStatus status,
            String qrCode
    ) implements Serializable {}
}
