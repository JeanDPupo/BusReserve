package co.edu.unimagdalena.busreserve.api.dto;

import co.edu.unimagdalena.busreserve.domine.entities.EntityType;
import co.edu.unimagdalena.busreserve.domine.entities.IncidentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class IncidentDtos {
    public record IncidentCreateRequest(
            @NotNull EntityType entityType,
            @NotNull Long entityId,
            @NotNull IncidentType type,
            @NotBlank String note
    ) implements Serializable {}

    public record IncidentResponse(
            Long id,
            EntityType entityType,
            Long entityId,
            IncidentType type,
            String note,
            LocalDateTime createdAt
    ) implements Serializable {}
}