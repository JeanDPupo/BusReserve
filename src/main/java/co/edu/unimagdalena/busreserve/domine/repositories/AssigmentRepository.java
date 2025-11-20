package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssigmentRepository extends JpaRepository<Assignment,Long> {
    // Obtener asignaciones por viaje
    List<Assignment> findByTripId(Long tripId);

    // Obtener asignaciones por conductor
    List<Assignment> findByDriverId(Long driverId);

    // Obtener asignaciones por despachador
    List<Assignment> findByDispatcherId(Long dispatcherId);
}
