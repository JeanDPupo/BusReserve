package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FareRuleRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private FareRuleRepository fareRuleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    private Route route;
    private Stop s1, s2, s3;

    private FareRule fr1; // tramo 1 → 2
    private FareRule fr2; // tramo 2 → 3
    private FareRule fr3; // tramo 1 → 3

    @BeforeEach
    void setUp() {

        // ==========================================
        // GIVEN: Crear Route
        // ==========================================
        route = routeRepository.save(
                Route.builder()
                        .code("R500")
                        .name("Bogotá - Tunja")
                        .origin("Bogotá")
                        .destination("Tunja")
                        .distanceKm(150.0)
                        .durationMin(160)
                        .build()
        );

        // ==========================================
        // GIVEN: Crear Stops
        // ==========================================
        s1 = stopRepository.save(
                Stop.builder()
                        .name("Salitre")
                        .stopOrder(1)
                        .lat(4.65)
                        .lng(-74.10)
                        .route(route)
                        .build()
        );

        s2 = stopRepository.save(
                Stop.builder()
                        .name("Briceño")
                        .stopOrder(2)
                        .lat(4.90)
                        .lng(-73.95)
                        .route(route)
                        .build()
        );

        s3 = stopRepository.save(
                Stop.builder()
                        .name("Puente Boyacá")
                        .stopOrder(3)
                        .lat(5.52)
                        .lng(-73.37)
                        .route(route)
                        .build()
        );

        // ==========================================
        // GIVEN: Crear FareRules por tramo
        // ==========================================
        fr1 = fareRuleRepository.save(
                FareRule.builder()
                        .route(route)
                        .fromStop(s1)
                        .toStop(s2)
                        .basePrice(new BigDecimal("10000"))
                        .discounts(null)
                        .dynamicPricing(false)
                        .build()
        );

        fr2 = fareRuleRepository.save(
                FareRule.builder()
                        .route(route)
                        .fromStop(s2)
                        .toStop(s3)
                        .basePrice(new BigDecimal("12000"))
                        .discounts(null)
                        .dynamicPricing(false)
                        .build()
        );

        fr3 = fareRuleRepository.save(
                FareRule.builder()
                        .route(route)
                        .fromStop(s1)
                        .toStop(s3)
                        .basePrice(new BigDecimal("17000"))
                        .discounts(null)
                        .dynamicPricing(true)
                        .build()
        );
    }


    // ============================================================================
    // TEST 1: findByRouteId → obtener todas las tarifas de la ruta
    // ============================================================================
    @Test
    void shouldFindFareRulesByRouteId() {

        List<FareRule> result = fareRuleRepository.findByRouteId(route.getId());

        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(fr -> fr.getRoute().getId().equals(route.getId())));
    }



    // ============================================================================
    // TEST 2: findByRouteAndStops (tramo exacto: 1 → 2)
    // ============================================================================
    @Test
    void shouldFindFareRuleForExactSegment() {

        Optional<FareRule> result = fareRuleRepository.findByRouteAndStops(
                route.getId(),
                s1.getId(),
                s2.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(fr1.getId(), result.get().getId());
    }


    // ============================================================================
    // TEST 3: findByRouteAndStops (segmento mayor: 1 → 3)
    // ============================================================================
    @Test
    void shouldFindFareRuleForLongSegment() {

        Optional<FareRule> result = fareRuleRepository.findByRouteAndStops(
                route.getId(),
                s1.getId(),
                s3.getId()
        );

        assertTrue(result.isPresent());
        assertEquals(fr3.getId(), result.get().getId());
    }


    // ============================================================================
    // TEST 4: findByRouteAndStops devuelve vacío cuando no existe la tarifa
    // ============================================================================
    @Test
    void shouldReturnEmptyWhenFareRuleDoesNotExist() {

        Optional<FareRule> result = fareRuleRepository.findByRouteAndStops(
                route.getId(),
                s3.getId(),  // tramo 3 → 1 no existe
                s1.getId()
        );

        assertTrue(result.isEmpty());
    }
}
