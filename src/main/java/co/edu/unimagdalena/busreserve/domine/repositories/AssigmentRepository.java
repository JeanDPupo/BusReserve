package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import co.edu.unimagdalena.busreserve.domine.entities.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssigmentRepository extends JpaRepository<Baggage,Long> {
    Optional<Assignment> findByTripId(Long tripId);
    List<Assignment> findByDriverId(Long driverId);
    List<Assignment> findByDispatcherId(Long dispatcherId);
}
