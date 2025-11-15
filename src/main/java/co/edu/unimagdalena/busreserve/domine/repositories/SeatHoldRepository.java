package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import co.edu.unimagdalena.busreserve.domine.entities.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatHoldRepository extends JpaRepository<SeatHold,Long> {
    List<SeatHold> findByTripIdAndSeatNumberAndStatus(Long tripId, String seatNumber, HoldStatus status);

    @Query("""
        SELECT sh FROM SeatHold sh
        WHERE sh.trip.id = :tripId 
          AND sh.seatNumber = :seatNumber 
          AND (sh.status = 'HOLD' AND sh.expiresAt > :now 
               OR sh.status = 'EXPIRED')
        """)
    Optional<SeatHold> findActiveOrExpiredHold(
            @Param("tripId") Long tripId,
            @Param("seatNumber") String seatNumber,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("UPDATE SeatHold sh SET sh.status = 'EXPIRED' WHERE sh.expiresAt < :now AND sh.status = 'HOLD'")
    int expireHolds(@Param("now") LocalDateTime now);

    List<SeatHold> findByExpiresAtBeforeAndStatus(LocalDateTime threshold, HoldStatus status);
}
