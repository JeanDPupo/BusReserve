package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.TripDtos.*;
        import co.edu.unimagdalena.busreserve.domine.entities.*;
        import co.edu.unimagdalena.busreserve.domine.repositories.*;
        import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.TripMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripRepository tripRepo;
    @Mock
    private RouteRepository routeRepo;
    @Mock
    private BusRepository busRepo;
    @Spy
    private TripMapper mapper = Mappers.getMapper(TripMapper.class);
    @InjectMocks
    private TripServiceImpl service;

    @Test
    void create_shouldCreateTripWhenBusIsAvailable() {
        var now = LocalDateTime.now();
        var req = new TripCreateRequest(1L, 2L, now, now.plusHours(1), now.plusHours(16));

        var route = Route.builder().id(1L).code("STMR-BGT").build();
        var bus = Bus.builder().id(2L).plate("ABC-123").available(true).build();

        when(routeRepo.findById(1L)).thenReturn(Optional.of(route));
        when(busRepo.findById(2L)).thenReturn(Optional.of(bus));
        when(tripRepo.save(any())).thenAnswer(inv -> {
            Trip t = inv.getArgument(0);
            t.setId(10L);
            return t;
        });

        TripResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.routeId()).isEqualTo(1L);
        assertThat(result.busId()).isEqualTo(2L);
        assertThat(result.status()).isEqualTo(TripStatus.SCHEDULED);
        verify(tripRepo).save(any(Trip.class));
    }

    @Test
    void create_shouldThrowExceptionWhenBusNotAvailable() {
        var now = LocalDateTime.now();
        var req = new TripCreateRequest(1L, 2L, now, now.plusHours(1), now.plusHours(16));

        var route = Route.builder().id(1L).build();
        var bus = Bus.builder().id(2L).plate("ABC-123").available(false).build();

        when(routeRepo.findById(1L)).thenReturn(Optional.of(route));
        when(busRepo.findById(2L)).thenReturn(Optional.of(bus));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not available");

        verify(tripRepo, never()).save(any());
    }

    @Test
    void openBoarding_shouldChangeStatusToBoarding() {
        var trip = Trip.builder()
                .id(5L)
                .status(TripStatus.SCHEDULED)
                .build();

        when(tripRepo.findById(5L)).thenReturn(Optional.of(trip));
        when(tripRepo.save(any())).thenReturn(trip);

        service.openBoarding(5L);

        assertThat(trip.getStatus()).isEqualTo(TripStatus.BOARDING);
        verify(tripRepo).save(trip);
    }

    @Test
    void openBoarding_shouldThrowExceptionWhenNotScheduled() {
        var trip = Trip.builder()
                .id(5L)
                .status(TripStatus.DEPARTED)
                .build();

        when(tripRepo.findById(5L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> service.openBoarding(5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("scheduled trips");

        verify(tripRepo, never()).save(any());
    }

    @Test
    void depart_shouldChangeStatusToDeparted() {
        var trip = Trip.builder()
                .id(5L)
                .status(TripStatus.BOARDING)
                .build();

        when(tripRepo.findById(5L)).thenReturn(Optional.of(trip));
        when(tripRepo.save(any())).thenReturn(trip);

        service.depart(5L);

        assertThat(trip.getStatus()).isEqualTo(TripStatus.DEPARTED);
        verify(tripRepo).save(trip);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotScheduled() {
        var trip = Trip.builder()
                .id(7L)
                .status(TripStatus.BOARDING)
                .build();

        when(tripRepo.findById(7L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> service.delete(7L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot delete trip");

        verify(tripRepo, never()).deleteById(any());
    }
}
