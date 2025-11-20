package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    // Obtener todas las rutas
    List<Route> findAll();

    Optional<Route> findRouteById(Long id);

    // Obtener rutas por origen
    List<Route> findByOriginId(Long originId);

    // Obtener rutas por destino
    List<Route> findByDestinationId(Long destinationId);
}
