package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.RouteDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class RouteMapperTest {

    private final RouteMapper mapper = Mappers.getMapper(RouteMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new RouteCreateRequest("STMR-BGT", "Santa Marta - Bogotá", "Santa Marta", "Bogotá", 950.0, 900);
        
        Route entity = mapper.toEntity(req);
        
        assertThat(entity.getCode()).isEqualTo("STMR-BGT");
        assertThat(entity.getName()).isEqualTo("Santa Marta - Bogotá");
        assertThat(entity.getOrigin()).isEqualTo("Santa Marta");
        assertThat(entity.getDestination()).isEqualTo("Bogotá");
        assertThat(entity.getDistanceKm()).isEqualTo(950.0);
        assertThat(entity.getDurationMin()).isEqualTo(900);
    }

    @Test
    void toResponse_shouldMapEntity() {
        var entity = Route.builder()
                .id(1L)
                .code("STMR-CTG")
                .name("Santa Marta - Cartagena")
                .origin("Santa Marta")
                .destination("Cartagena")
                .distanceKm(240.0)
                .durationMin(300)
                .build();
        
        RouteResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.code()).isEqualTo("STMR-CTG");
        assertThat(dto.origin()).isEqualTo("Santa Marta");
        assertThat(dto.destination()).isEqualTo("Cartagena");
    }

    @Test
    void patch_shouldIgnoreCodeAndNulls() {
        var entity = Route.builder()
                .id(3L)
                .code("OLD-CODE")
                .name("Old Name")
                .origin("Origin A")
                .destination("Destination B")
                .distanceKm(500.0)
                .durationMin(400)
                .build();
        
        var changes = new RouteUpdateRequest("New Name", null, "New Destination", null, 450);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getCode()).isEqualTo("OLD-CODE");
        assertThat(entity.getName()).isEqualTo("New Name");
        assertThat(entity.getOrigin()).isEqualTo("Origin A");
        assertThat(entity.getDestination()).isEqualTo("New Destination");
        assertThat(entity.getDistanceKm()).isEqualTo(500.0);
        assertThat(entity.getDurationMin()).isEqualTo(450);
    }
}
