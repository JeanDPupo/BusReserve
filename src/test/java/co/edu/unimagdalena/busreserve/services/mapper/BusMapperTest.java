package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.BusDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class BusMapperTest {

    private final BusMapper mapper = Mappers.getMapper(BusMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithDefaultAvailable() {
        var req = new BusCreateRequest("ABC-123", 45, "{\"wifi\": true, \"ac\": true}");
        
        Bus entity = mapper.toEntity(req);
        
        assertThat(entity.getPlate()).isEqualTo("ABC-123");
        assertThat(entity.getCapacity()).isEqualTo(45);
        assertThat(entity.getAmenities()).isEqualTo("{\"wifi\": true, \"ac\": true}");
        assertThat(entity.getAvailable()).isTrue();
    }

    @Test
    void toResponse_shouldMapEntity() {
        var entity = Bus.builder()
                .id(1L)
                .plate("XYZ-789")
                .capacity(50)
                .amenities("{\"tv\": true}")
                .available(false)
                .build();
        
        BusResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.plate()).isEqualTo("XYZ-789");
        assertThat(dto.capacity()).isEqualTo(50);
        assertThat(dto.available()).isFalse();
    }

    @Test
    void patch_shouldIgnorePlateAndUpdateOthers() {
        var entity = Bus.builder()
                .id(2L)
                .plate("OLD-PLATE")
                .capacity(40)
                .amenities("{}")
                .available(true)
                .build();
        
        var changes = new BusUpdateRequest(48, "{\"wifi\": true}", false);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getPlate()).isEqualTo("OLD-PLATE");
        assertThat(entity.getCapacity()).isEqualTo(48);
        assertThat(entity.getAmenities()).isEqualTo("{\"wifi\": true}");
        assertThat(entity.getAvailable()).isFalse();
    }
}
