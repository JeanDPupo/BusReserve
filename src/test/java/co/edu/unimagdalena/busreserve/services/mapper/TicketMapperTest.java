package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.TicketDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class TicketMapperTest {

    private final TicketMapper mapper = Mappers.getMapper(TicketMapper.class);

    @Test
    void toEntity_shouldMapCreateRequestWithDefaultStatus() {
        var req = new TicketCreateRequest(1L, 2L, "A12", 1L, 3L, 50000.0, PaymentMethod.CARD);
        
        Ticket entity = mapper.toEntity(req);
        
        assertThat(entity.getSeatNumber()).isEqualTo("A12");
        assertThat(entity.getFromStopId()).isEqualTo(1L);
        assertThat(entity.getToStopId()).isEqualTo(3L);
        assertThat(entity.getPrice()).isEqualTo(50000.0);
        assertThat(entity.getPaymentMethod()).isEqualTo(PaymentMethod.CARD);
        assertThat(entity.getStatus()).isEqualTo(TicketStatus.SOLD);
    }

    @Test
    void toResponse_shouldMapEntityWithRelations() {
        var trip = Trip.builder().id(5L).build();
        var passenger = User.builder().id(3L).name("Carlos Ruiz").build();
        
        var entity = Ticket.builder()
                .id(20L)
                .trip(trip)
                .passenger(passenger)
                .seatNumber("B08")
                .fromStopId(1L)
                .toStopId(4L)
                .price(75000.0)
                .paymentMethod(PaymentMethod.CASH)
                .status(TicketStatus.SOLD)
                .qrCode("QR-123456")
                .build();
        
        TicketResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(20L);
        assertThat(dto.tripId()).isEqualTo(5L);
        assertThat(dto.passengerId()).isEqualTo(3L);
        assertThat(dto.passengerName()).isEqualTo("Carlos Ruiz");
        assertThat(dto.seatNumber()).isEqualTo("B08");
        assertThat(dto.qrCode()).isEqualTo("QR-123456");
    }

    @Test
    void patch_shouldOnlyUpdateStatus() {
        var entity = Ticket.builder()
                .id(15L)
                .seatNumber("C05")
                .status(TicketStatus.SOLD)
                .build();
        
        var changes = new TicketUpdateRequest(TicketStatus.CANCELLED);
        
        mapper.patch(entity, changes);
        
        assertThat(entity.getStatus()).isEqualTo(TicketStatus.CANCELLED);
        assertThat(entity.getSeatNumber()).isEqualTo("C05");
    }
}
