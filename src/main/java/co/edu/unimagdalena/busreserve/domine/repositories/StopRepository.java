package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StopRepository extends JpaRepository<Stop,Long> {

    List<Stop> findByRouteId(Long routeId);  // Obtener paradas por ruta

    List<Stop> findByNameContainingIgnoreCase(String namePart);  // Buscar por nombre de la parada
}
