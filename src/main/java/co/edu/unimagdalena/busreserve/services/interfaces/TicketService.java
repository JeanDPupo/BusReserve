package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.TicketDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {
    TicketResponse create(TicketCreateRequest req);
    TicketResponse get(Long id);
    TicketResponse getByQrCode(String qrCode);
    List<TicketResponse> findByTrip(Long tripId);
    List<TicketResponse> findByPassenger(Long passengerId);
    List<TicketResponse> findByStatus(TicketStatus status);
    TicketResponse update(Long id, TicketUpdateRequest req);
    void cancel(Long id);
    void markAsNoShow(Long id);
    void validateTicket(String qrCode);
    Long countSoldSeatsByTrip(Long tripId);
}
