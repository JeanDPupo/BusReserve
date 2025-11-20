package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.HoldStatus;
import co.edu.unimagdalena.busreserve.domine.entities.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface SeatHoldRepository extends JpaRepository<SeatHold,Long> {
    // Buscar una reserva de asiento por viaje y número de asiento
    Optional<SeatHold> findByTripIdAndSeatNumber(Long tripId, String seatNumber);

    // Buscar las reservas activas para un usuario
    List<SeatHold> findByUserIdAndStatus(Long userId, HoldStatus status);
}
