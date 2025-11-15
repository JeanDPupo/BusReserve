package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.StopDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class StopMapperTest {

    private final StopMapper mapper = Mappers.getMapper(StopMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new StopCreateRequest(1L, "Barranquilla Terminal", 1, 10.9685, -74.7813);
        
        Stop entity = mapper.toEntity(req);
        
        assertThat(entity.getName()).isEqualTo("Barranquilla Terminal");
        assertThat(entity.getOrderIndex()).isEqualTo(1);
        assertThat(entity.getLat()).isEqualTo(10.9685);
        assertThat(entity.getLng()).isEqualTo(-74.7813);
    }

    @Test
    void toResponse_shouldMapEntityWithRouteId() {
        var route = Route.builder().id(5L).code("STMR-BGT").build();
        var entity = Stop.builder()
                .id(10L)
                .route(route)
                .name("Fundación")
                .orderIndex(2)
                .lat(10.5209)
                .lng(-74.1851)
                .build();
        
        StopResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.routeId()).isEqualTo(5L);
        assertThat(dto.name()).isEqualTo("Fundación");
        assertThat(dto.order()).isEqualTo(2);
    }

    @Test
    void patch_shouldUpdateOnlyProvidedFields() {
        var entity = Stop.builder()
                .id(7L)
                .name("Old Stop")
                .orderIndex(1)
                .lat(10.0)
                .lng(-74.0)
                .build();
        
        var changes = new StopUpdateRequest("New Stop", null, 11.0, null);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getName()).isEqualTo("New Stop");
        assertThat(entity.getOrderIndex()).isEqualTo(1);
        assertThat(entity.getLat()).isEqualTo(11.0);
        assertThat(entity.getLng()).isEqualTo(-74.0);
    }
}
