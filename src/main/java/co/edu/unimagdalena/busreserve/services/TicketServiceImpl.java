package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.TicketDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.*;
import co.edu.unimagdalena.busreserve.domine.repositories.TicketRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TripRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.UserRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.TicketService;
import co.edu.unimagdalena.busreserve.services.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;
    private final TicketMapper mapper;

    @Override
    public TicketResponse create(TicketCreateRequest req) {
        Trip trip = tripRepo.findById(req.tripId())
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(req.tripId())));

        if (trip.getStatus() == TripStatus.DEPARTED || trip.getStatus() == TripStatus.ARRIVED) {
            throw new IllegalStateException("Cannot book tickets for trips that have departed or arrived");
        }

        User passenger = userRepo.findById(req.passengerId())
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(req.passengerId())));

        if (passenger.getRole() != Role.PASSENGER) {
            throw new IllegalStateException("User must be a passenger to book tickets");
        }

        List<Ticket> overlappingTickets = ticketRepo.findOverlappingTickets(
                req.tripId(), req.seatNumber(), req.fromStopId(), req.toStopId()
        );

        if (!overlappingTickets.isEmpty()) {
            throw new IllegalStateException("Seat %s is already booked for this route segment".formatted(req.seatNumber()));
        }

        Ticket ticket = mapper.toEntity(req);
        ticket.setTrip(trip);
        ticket.setPassenger(passenger);
        ticket.setQrCode(UUID.randomUUID().toString());
        ticket.setStatus(TicketStatus.SOLD);

        return mapper.toResponse(ticketRepo.save(ticket));
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse get(Long id) {
        return ticketRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Ticket %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getByQrCode(String qrCode) {
        return ticketRepo.findAll().stream()
                .filter(t -> qrCode.equals(t.getQrCode()))
                .findFirst()
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Ticket with QR code not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> findByTrip(Long tripId) {
        return ticketRepo.findByTripId(tripId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> findByPassenger(Long passengerId) {
        return ticketRepo.findByPassengerId(passengerId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> findByStatus(TicketStatus status) {
        return ticketRepo.findByTripIdAndStatusIn(null, List.of(status))
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public TicketResponse update(Long id, TicketUpdateRequest req) {
        Ticket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket %d not found".formatted(id)));

        mapper.patch(ticket, req);

        return mapper.toResponse(ticketRepo.save(ticket));
    }

    @Override
    public void cancel(Long id) {
        Ticket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket %d not found".formatted(id)));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled");
        }

        if (ticket.getTrip().getStatus() == TripStatus.DEPARTED) {
            throw new IllegalStateException("Cannot cancel ticket for departed trip");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepo.save(ticket);
    }

    @Override
    public void markAsNoShow(Long id) {
        Ticket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket %d not found".formatted(id)));

        ticket.setStatus(TicketStatus.NO_SHOW);
        ticketRepo.save(ticket);
    }

    @Override
    public void validateTicket(String qrCode) {
        Ticket ticket = ticketRepo.findAll().stream()
                .filter(t -> qrCode.equals(t.getQrCode()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Ticket with QR code not found"));

        if (ticket.getStatus() != TicketStatus.SOLD) {
            throw new IllegalStateException("Ticket is not valid for boarding");
        }

        if (ticket.getTrip().getStatus() != TripStatus.BOARDING) {
            throw new IllegalStateException("Trip is not in boarding status");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSoldSeatsByTrip(Long tripId) {
        return ticketRepo.countSoldSeats(tripId);
    }
}