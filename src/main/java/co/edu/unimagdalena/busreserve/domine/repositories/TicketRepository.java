package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Ticket;
import co.edu.unimagdalena.busreserve.domine.entities.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByTripId(Long tripId);
    List<Ticket> findByPassengerId(Long passengerId);
    List<Ticket> findByTripIdAndStatusIn(Long tripId, List<TicketStatus> statuses);

    @Query("""
        SELECT t FROM Ticket t 
        WHERE t.trip.id = :tripId 
          AND t.seatNumber = :seatNumber 
          AND t.fromStopId <= :toStopId 
          AND t.toStopId >= :fromStopId
        """)
    List<Ticket> findOverlappingTickets(
            @Param("tripId") Long tripId,
            @Param("seatNumber") String seatNumber,
            @Param("fromStopId") Long fromStopId,
            @Param("toStopId") Long toStopId
    );

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.trip.id = :tripId AND t.status = 'SOLD'")
    long countSoldSeats(@Param("tripId") Long tripId);

    List<Ticket> findByStatusAndTrip_DepartureAtBefore(TicketStatus status, LocalDateTime threshold);
}
