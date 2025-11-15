package co.edu.unimagdalena.busreserve.services.mapper;

import co.edu.unimagdalena.busreserve.api.dto.BaggageDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import co.edu.unimagdalena.busreserve.domine.entities.Ticket;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class BaggageMapperTest {

    private final BaggageMapper mapper = Mappers.getMapper(BaggageMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new BaggageCreateRequest(15.5, 10000.0, "BAG-12345");
        
        Baggage entity = mapper.toEntity(req);
        
        assertThat(entity.getWeightKg()).isEqualTo(15.5);
        assertThat(entity.getFee()).isEqualTo(10000.0);
        assertThat(entity.getTagCode()).isEqualTo("BAG-12345");
    }

    @Test
    void toResponse_shouldMapEntityWithTicketId() {
        var ticket = Ticket.builder().id(20L).build();
        var entity = Baggage.builder()
                .id(10L)
                .ticket(ticket)
                .weightKg(22.0)
                .fee(15000.0)
                .tagCode("BAG-67890")
                .build();
        
        BaggageResponse dto = mapper.toResponse(entity);
        
        assertThat(dto.id()).isEqualTo(10L);
        assertThat(dto.ticketId()).isEqualTo(20L);
        assertThat(dto.weightKg()).isEqualTo(22.0);
        assertThat(dto.fee()).isEqualTo(15000.0);
        assertThat(dto.tagCode()).isEqualTo("BAG-67890");
    }
}