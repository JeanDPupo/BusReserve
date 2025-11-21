package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StopRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private RouteRepository routeRepository;

    private Route routeA;
    private Stop s1;
    private Stop s2;
    private Stop s3;

    @BeforeEach
    void setUp() {

        // ============================
        // GIVEN: Crear una Route real
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

        // ============================
        // Crear Stops asociados
        // ============================

        s1 = stopRepository.save(
                Stop.builder()
                        .name("Terminal Salitre")
                        .stopOrder(1)
                        .lat(4.65)
                        .lng(-74.10)
                        .route(routeA)
                        .build()
        );

        s2 = stopRepository.save(
                Stop.builder()
                        .name("Briceño")
                        .stopOrder(2)
                        .lat(4.93)
                        .lng(-73.95)
                        .route(routeA)
                        .build()
        );

        s3 = stopRepository.save(
                Stop.builder()
                        .name("Puente Boyacá")
                        .stopOrder(3)
                        .lat(5.52)
                        .lng(-73.37)
                        .route(routeA)
                        .build()
        );
    }


    // =======================================================
    // TESTS: findByRouteId
    // =======================================================

    @Test
    void shouldFindStopsByRouteId() {
        // WHEN
        List<Stop> stops = stopRepository.findByRouteId(routeA.getId());

        // THEN
        assertEquals(3, stops.size());
        assertTrue(stops.stream().allMatch(st -> st.getRoute().getId().equals(routeA.getId())));
    }

    @Test
    void shouldReturnEmptyListForNonExistingRouteId() {
        // WHEN
        List<Stop> stops = stopRepository.findByRouteId(999L);

        // THEN
        assertTrue(stops.isEmpty());
    }


    // =======================================================
    // TESTS: findByNameContainingIgnoreCase
    // =======================================================

    @Test
    void shouldFindStopsByNameContainingIgnoreCase() {
        // WHEN
        List<Stop> stops = stopRepository.findByNameContainingIgnoreCase("terminal");

        // THEN
        assertEquals(1, stops.size());
        assertEquals("Terminal Salitre", stops.get(0).getName());
    }

    @Test
    void shouldFindMultipleStopsMatchingNamePart() {
        // GIVEN: s1 = "Terminal Salitre", s2 = "Briceño", s3 = "Puente Boyacá"
        // WHEN
        List<Stop> stops = stopRepository.findByNameContainingIgnoreCase("e");

        // THEN (Terminal "e", Briceño "e", Puente Boyacá "e")
        assertEquals(3, stops.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoStopMatchesName() {
        // WHEN
        List<Stop> stops = stopRepository.findByNameContainingIgnoreCase("ZZZZZ");

        // THEN
        assertTrue(stops.isEmpty());
    }
}
