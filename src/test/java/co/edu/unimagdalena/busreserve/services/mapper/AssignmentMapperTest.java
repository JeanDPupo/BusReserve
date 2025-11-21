package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.AssignmentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AssignmentMapperTest {

    private final AssignmentMapper mapper = Mappers.getMapper(AssignmentMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithAutoAssignedAt() {
        var req = new AssignmentCreateRequest(1L, 2L, 3L, false);
        
        Assignment entity = mapper.toEntity(req);
        
        assertThat(entity.getChecklistOk()).isFalse();
        assertThat(entity.getAssignedAt()).isNotNull();
    }

    @Test
    void toResponse_shouldMapEntityWithRelations() {
        var trip = Trip.builder().id(5L).build();
        var driver = User.builder().id(10L).name("Carlos Perez").build();
        var dispatcher = User.builder().id(15L).name("Ana Lopez").build();
        var assignedAt = LocalDateTime.now();
        
        var entity = Assignment.builder()
                .id(20L)
                .trip(trip)
                .driver(driver)
                .dispatcher(dispatcher)
                .checklistOk(true)
                .assignedAt(assignedAt)
                .build();
        
        AssignmentResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(20L);
        assertThat(dto.tripId()).isEqualTo(5L);
        assertThat(dto.driverId()).isEqualTo(10L);
        assertThat(dto.driverName()).isEqualTo("Carlos Perez");
        assertThat(dto.dispatcherId()).isEqualTo(15L);
        assertThat(dto.dispatcherName()).isEqualTo("Ana Lopez");
        assertThat(dto.checklistOk()).isTrue();
        assertThat(dto.assignedAt()).isEqualTo(assignedAt);
    }

    @Test
    void patch_shouldOnlyUpdateChecklistOk() {
        var entity = Assignment.builder()
                .id(8L)
                .checklistOk(false)
                .assignedAt(LocalDateTime.now())
                .build();
        
        var changes = new AssignmentUpdateRequest(true);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getChecklistOk()).isTrue();
    }
}