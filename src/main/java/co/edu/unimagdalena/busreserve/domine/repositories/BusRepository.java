package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus,Long> {
    List<Bus> findByAvailableTrue();
    Optional<Bus> findByPlate(String plate);
}
