package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.IncidentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.EntityType;
import co.edu.unimagdalena.busreserve.domine.entities.Incident;
import co.edu.unimagdalena.busreserve.domine.entities.IncidentType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentMapperTest {

    private final IncidentMapper mapper = Mappers.getMapper(IncidentMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithAutoCreatedAt() {
        var req = new IncidentCreateRequest(
                EntityType.TRIP,
                10L,
                IncidentType.VEHICLE,
                "Motor overheating detected"
        );
        
        Incident entity = mapper.toEntity(req);
        
        assertThat(entity.getEntityType()).isEqualTo(EntityType.TRIP);
        assertThat(entity.getEntityId()).isEqualTo(10L);
        assertThat(entity.getType()).isEqualTo(IncidentType.VEHICLE);
        assertThat(entity.getNote()).isEqualTo("Motor overheating detected");
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    void toResponse_shouldMapEntity() {
        var entity = Incident.builder()
                .id(5L)
                .entityType(EntityType.PARCEL)
                .entityId(20L)
                .type(IncidentType.DELIVERY_FAIL)
                .note("Recipient not found at address")
                .createdAt(java.time.LocalDateTime.now())
                .build();
        
        IncidentResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.entityType()).isEqualTo(EntityType.PARCEL);
        assertThat(dto.entityId()).isEqualTo(20L);
        assertThat(dto.type()).isEqualTo(IncidentType.DELIVERY_FAIL);
        assertThat(dto.note()).isEqualTo("Recipient not found at address");
        assertThat(dto.createdAt()).isNotNull();
    }
}