package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.TripDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TripMapperTest {

    private final TripMapper mapper = Mappers.getMapper(TripMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithDefaultStatus() {
        var now = LocalDateTime.now();
        var req = new TripCreateRequest(1L, 2L, now, now.plusHours(1), now.plusHours(16));
        
        Trip entity = mapper.toEntity(req);
        
        assertThat(entity.getDate()).isEqualTo(now);
        assertThat(entity.getDepartureAt()).isEqualTo(now.plusHours(1));
        assertThat(entity.getArrivalEta()).isEqualTo(now.plusHours(16));
        assertThat(entity.getStatus()).isEqualTo(TripStatus.SCHEDULED);
    }

    @Test
    void toResponse_shouldMapEntityWithRelations() {
        var route = Route.builder().id(3L).code("STMR-BGT").build();
        var bus = Bus.builder().id(5L).plate("BUS-456").build();
        var now = LocalDateTime.now();
        
        var entity = Trip.builder()
                .id(10L)
                .route(route)
                .bus(bus)
                .date(now)
                .departureAt(now.plusHours(2))
                .arrivalEta(now.plusHours(18))
                .status(TripStatus.BOARDING)
                .build();
        
        TripResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.routeId()).isEqualTo(3L);
        assertThat(dto.routeCode()).isEqualTo("STMR-BGT");
        assertThat(dto.busId()).isEqualTo(5L);
        assertThat(dto.busPlate()).isEqualTo("BUS-456");
        assertThat(dto.status()).isEqualTo(TripStatus.BOARDING);
    }

    @Test
    void patch_shouldUpdateOnlyStatus() {
        var entity = Trip.builder()
                .id(7L)
                .date(LocalDateTime.now())
                .status(TripStatus.SCHEDULED)
                .build();
        
        var changes = new TripUpdateRequest(null, null, null, TripStatus.DEPARTED);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getStatus()).isEqualTo(TripStatus.DEPARTED);
    }
}
