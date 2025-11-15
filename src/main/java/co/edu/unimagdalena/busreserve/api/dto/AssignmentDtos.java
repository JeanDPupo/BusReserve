package co.edu.unimagdalena.busreserve.api.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AssignmentDtos {
    public record AssignmentCreateRequest(
            @NotNull Long tripId,
            @NotNull Long driverId,
            @NotNull Long dispatcherId,
            Boolean checklistOk
    ) implements Serializable {}

    public record AssignmentUpdateRequest(
            Boolean checklistOk
    ) implements Serializable {}

    public record AssignmentResponse(
            Long id,
            Long tripId,
            Long driverId,
            String driverName,
            Long dispatcherId,
            String dispatcherName,
            Boolean checklistOk,
            LocalDateTime assignedAt
    ) implements Serializable {}
}
