package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteRepositoryTest extends AbstractRepositoryIT{

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    private Route routeA;
    private Route routeB;

    @BeforeEach
    void setUp() {

        // ============================
        // GIVEN: Crear rutas
        // ============================

        routeA = routeRepository.save(
                Route.builder()
                        .code("R001")
                        .name("Bogotá - Tunja")
                        .origin("Bogotá")
                        .destination("Tunja")
                        .distanceKm(150.0)
                        .durationMin(160)
                        .build()
        );

        routeB = routeRepository.save(
                Route.builder()
                        .code("R002")
                        .name("Bogotá - Duitama")
                        .origin("Bogotá")
                        .destination("Duitama")
                        .distanceKm(190.0)
                        .durationMin(210)
                        .build()
        );

        // ============================
        // Crear Stops para routeA
        // ============================

        stopRepository.save(
                Stop.builder()
                        .name("Terminal Salitre")
                        .stopOrder(1)
                        .lat(4.65)
                        .lng(-74.10)
                        .route(routeA)
                        .build()
        );

        stopRepository.save(
                Stop.builder()
                        .name("Puente Boyacá")
                        .stopOrder(2)
                        .lat(5.52)
                        .lng(-73.37)
                        .route(routeA)
                        .build()
        );
    }

    // =======================================================
    // TESTS
    // =======================================================

    @Test
    void shouldFindRouteById() {
        // WHEN
        Optional<Route> found = routeRepository.findById(routeA.getId());

        // THEN
        assertTrue(found.isPresent());
        assertEquals("Bogotá - Tunja", found.get().getName());
        assertEquals("Bogotá", found.get().getOrigin());
        assertEquals("Tunja", found.get().getDestination());
    }

    @Test
    void shouldFindRoutesByOrigin() {
        // WHEN
        List<Route> result = routeRepository.findByOrigin("Bogotá");

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(r -> r.getOrigin().equals("Bogotá")));
    }

    @Test
    void shouldFindRoutesByDestination() {
        // WHEN
        List<Route> result = routeRepository.findByDestination("Duitama");

        // THEN
        assertEquals(1, result.size());
        assertEquals(routeB.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnEmptyForNonExistingId() {
        // WHEN
        Optional<Route> found = routeRepository.findById(999L);

        // THEN
        assertTrue(found.isEmpty());
    }
}
