package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.SeatDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.entities.Seat;
import co.edu.unimagdalena.busreserve.domine.entities.SeatType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class SeatMapperTest {

    private final SeatMapper mapper = Mappers.getMapper(SeatMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new SeatCreateRequest(5L, "A12", SeatType.STANDARD);
        
        Seat entity = mapper.toEntity(req);
        
        assertThat(entity.getNumber()).isEqualTo("A12");
        assertThat(entity.getType()).isEqualTo(SeatType.STANDARD);
    }

    @Test
    void toResponse_shouldMapEntityWithBusId() {
        var bus = Bus.builder().id(3L).plate("ABC-123").build();
        var entity = Seat.builder()
                .id(10L)
                .bus(bus)
                .number("B05")
                .type(SeatType.PREFERENTIAL)
                .build();
        
        SeatResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.busId()).isEqualTo(3L);
        assertThat(dto.number()).isEqualTo("B05");
        assertThat(dto.type()).isEqualTo(SeatType.PREFERENTIAL);
    }
}