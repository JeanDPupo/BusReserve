package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.SeatDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.entities.Seat;
import co.edu.unimagdalena.busreserve.domine.entities.SeatType;
import co.edu.unimagdalena.busreserve.domine.repositories.BusRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.SeatRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.SeatMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepo;
    @Mock
    private BusRepository busRepo;
    @Spy
    private SeatMapper mapper = Mappers.getMapper(SeatMapper.class);
    @InjectMocks
    private SeatServiceImpl service;

    @Test
    void create_shouldCreateSeatWhenBusExists() {
        var req = new SeatCreateRequest(1L, "A12", SeatType.STANDARD);
        var bus = Bus.builder().id(1L).plate("ABC-123").build();

        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepo.findByBusIdAndNumber(1L, "A12")).thenReturn(Optional.empty());
        when(seatRepo.save(any())).thenAnswer(inv -> {
            Seat s = inv.getArgument(0);
            s.setId(10L);
            return s;
        });

        SeatResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.number()).isEqualTo("A12");
        verify(seatRepo).save(any(Seat.class));
    }

    @Test
    void create_shouldThrowExceptionWhenBusNotFound() {
        var req = new SeatCreateRequest(99L, "A12", SeatType.STANDARD);

        when(busRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Bus 99 not found");

        verify(seatRepo, never()).save(any());
    }

    @Test
    void create_shouldThrowExceptionWhenSeatAlreadyExists() {
        var req = new SeatCreateRequest(1L, "A12", SeatType.STANDARD);
        var bus = Bus.builder().id(1L).build();
        var existingSeat = Seat.builder().id(5L).number("A12").build();

        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepo.findByBusIdAndNumber(1L, "A12")).thenReturn(Optional.of(existingSeat));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists");

        verify(seatRepo, never()).save(any());
    }

    @Test
    void get_shouldReturnSeatWhenExists() {
        var seat = Seat.builder()
                .id(5L)
                .number("B08")
                .type(SeatType.PREFERENTIAL)
                .bus(Bus.builder().id(2L).build())
                .build();

        when(seatRepo.findById(5L)).thenReturn(Optional.of(seat));

        SeatResponse result = service.get(5L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.number()).isEqualTo("B08");
    }

    @Test
    void listByBus_shouldReturnSeatsForBus() {
        var seats = List.of(
                Seat.builder().id(1L).number("A01").bus(Bus.builder().id(5L).build()).build(),
                Seat.builder().id(2L).number("A02").bus(Bus.builder().id(5L).build()).build()
        );

        when(busRepo.existsById(5L)).thenReturn(true);
        when(seatRepo.findByBusId(5L)).thenReturn(seats);

        List<SeatResponse> result = service.listByBus(5L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SeatResponse::number).contains("A01", "A02");
    }

    @Test
    void listByBus_shouldThrowExceptionWhenBusNotFound() {
        when(busRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.listByBus(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createSeatsForBus_shouldCreateCorrectNumberOfSeats() {
        var bus = Bus.builder().id(1L).plate("ABC-123").build();

        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));
        when(seatRepo.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        service.createSeatsForBus(1L, 10);

        verify(seatRepo).saveAll(argThat(seats -> 
            ((List<Seat>) seats).size() == 10
        ));
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(seatRepo.existsById(7L)).thenReturn(true);

        service.delete(7L);

        verify(seatRepo).deleteById(7L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotExists() {
        when(seatRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class);

        verify(seatRepo, never()).deleteById(any());
    }
}