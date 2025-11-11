package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop,Long> {
    List<Stop> findByRouteIdOrderByOrderAsc(Long routeId);
    Optional<Stop> findByRouteIdAndOrder(Long routeId, Integer order);
}
