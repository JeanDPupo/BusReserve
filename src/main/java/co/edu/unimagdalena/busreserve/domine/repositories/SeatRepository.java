package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    // Obtener todas las sillas de un bus
    List<Seat> findByBusId(Long busId);

    // Buscar una silla específica en un bus
    Optional<Seat> findByBusIdAndNumber(Long busId, String seatNumber);
}
