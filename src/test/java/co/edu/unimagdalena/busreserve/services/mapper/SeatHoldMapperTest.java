package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.SeatHoldDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import co.edu.unimagdalena.busreserve.domine.entities.SeatHold;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SeatHoldMapperTest {

    private final SeatHoldMapper mapper = Mappers.getMapper(SeatHoldMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithDefaultStatus() {
        var expiresAt = LocalDateTime.now().plusMinutes(10);
        var req = new SeatHoldCreateRequest(1L, "A12", 5L, expiresAt);
        
        SeatHold entity = mapper.toEntity(req);
        
        assertThat(entity.getSeatNumber()).isEqualTo("A12");
        assertThat(entity.getStatus()).isEqualTo(HoldStatus.HOLD);
    }

    @Test
    void toResponse_shouldMapEntityWithRelations() {
        var trip = Trip.builder().id(10L).build();
        var user = User.builder().id(15L).build();
        var expiresAt = LocalDateTime.now().plusMinutes(10);
        
        var entity = SeatHold.builder()
                .id(20L)
                .trip(trip)
                .seatNumber("B08")
                .user(user)
                .expiresAt(expiresAt)
                .status(HoldStatus.HOLD)
                .build();
        
        SeatHoldResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(20L);
        assertThat(dto.tripId()).isEqualTo(10L);
        assertThat(dto.seatNumber()).isEqualTo("B08");
        assertThat(dto.userId()).isEqualTo(15L);
        assertThat(dto.expiresAt()).isEqualTo(expiresAt);
        assertThat(dto.status()).isEqualTo(HoldStatus.HOLD);
    }
}