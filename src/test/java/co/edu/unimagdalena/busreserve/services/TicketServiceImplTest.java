package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.TicketDtos.*;
        import co.edu.unimagdalena.busreserve.domine.entities.*;
        import co.edu.unimagdalena.busreserve.domine.repositories.*;
        import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.mapper.TicketMapper;
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
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepo;
    @Mock
    private TripRepository tripRepo;
    @Mock
    private UserRepository userRepo;
    @Spy
    private TicketMapper mapper = Mappers.getMapper(TicketMapper.class);
    @InjectMocks
    private TicketServiceImpl service;

    @Test
    void create_shouldCreateTicketWhenSeatIsAvailable() {
        var req = new TicketCreateRequest(1L, 2L, "A12", 1L, 3L, 50000.0, PaymentMethod.CARD);

        var trip = Trip.builder().id(1L).status(TripStatus.SCHEDULED).build();
        var passenger = User.builder().id(2L).role(Role.PASSENGER).build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(2L)).thenReturn(Optional.of(passenger));
        when(ticketRepo.findOverlappingTickets(anyLong(), anyString(), anyLong(), anyLong()))
                .thenReturn(List.of());
        when(ticketRepo.save(any())).thenAnswer(inv -> {
            Ticket t = inv.getArgument(0);
            t.setId(100L);
            return t;
        });

        TicketResponse result = service.create(req);

        assertThat(result.id()).isEqualTo(100L);
        assertThat(result.seatNumber()).isEqualTo("A12");
        assertThat(result.qrCode()).isNotNull();
        verify(ticketRepo).save(any(Ticket.class));
    }

    @Test
    void create_shouldThrowExceptionWhenSeatIsBooked() {
        var req = new TicketCreateRequest(1L, 2L, "A12", 1L, 3L, 50000.0, PaymentMethod.CARD);

        var trip = Trip.builder().id(1L).status(TripStatus.SCHEDULED).build();
        var passenger = User.builder().id(2L).role(Role.PASSENGER).build();
        var existingTicket = Ticket.builder().id(99L).build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(2L)).thenReturn(Optional.of(passenger));
        when(ticketRepo.findOverlappingTickets(1L, "A12", 1L, 3L))
                .thenReturn(List.of(existingTicket));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already booked");

        verify(ticketRepo, never()).save(any());
    }

    @Test
    void create_shouldThrowExceptionWhenUserNotPassenger() {
        var req = new TicketCreateRequest(1L, 2L, "A12", 1L, 3L, 50000.0, PaymentMethod.CARD);

        var trip = Trip.builder().id(1L).status(TripStatus.SCHEDULED).build();
        var driver = User.builder().id(2L).role(Role.DRIVER).build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(2L)).thenReturn(Optional.of(driver));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be a passenger");
    }

    @Test
    void create_shouldThrowExceptionWhenTripDeparted() {
        var req = new TicketCreateRequest(1L, 2L, "A12", 1L, 3L, 50000.0, PaymentMethod.CARD);

        var trip = Trip.builder().id(1L).status(TripStatus.DEPARTED).build();

        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("departed or arrived");
    }

    @Test
    void cancel_shouldChangeStatusToCancelled() {
        var ticket = Ticket.builder()
                .id(5L)
                .status(TicketStatus.SOLD)
                .trip(Trip.builder().status(TripStatus.SCHEDULED).build())
                .build();

        when(ticketRepo.findById(5L)).thenReturn(Optional.of(ticket));
        when(ticketRepo.save(any())).thenReturn(ticket);

        service.cancel(5L);

        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.CANCELLED);
        verify(ticketRepo).save(ticket);
    }

    @Test
    void cancel_shouldThrowExceptionWhenAlreadyCancelled() {
        var ticket = Ticket.builder()
                .id(5L)
                .status(TicketStatus.CANCELLED)
                .build();

        when(ticketRepo.findById(5L)).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> service.cancel(5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already cancelled");

        verify(ticketRepo, never()).save(any());
    }

    @Test
    void validateTicket_shouldPassWhenValid() {
        var ticket = Ticket.builder()
                .id(10L)
                .qrCode("QR-12345")
                .status(TicketStatus.SOLD)
                .trip(Trip.builder().status(TripStatus.BOARDING).build())
                .build();

        when(ticketRepo.findAll()).thenReturn(List.of(ticket));

        assertThatCode(() -> service.validateTicket("QR-12345"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateTicket_shouldThrowExceptionWhenNotBoarding() {
        var ticket = Ticket.builder()
                .id(10L)
                .qrCode("QR-12345")
                .status(TicketStatus.SOLD)
                .trip(Trip.builder().status(TripStatus.SCHEDULED).build())
                .build();

        when(ticketRepo.findAll()).thenReturn(List.of(ticket));

        assertThatThrownBy(() -> service.validateTicket("QR-12345"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not in boarding");
    }
}
