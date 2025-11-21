package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.TripStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TripRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private BusRepository busRepository;

    private Route routeA;
    private Route routeB;
    private Bus bus;
    private Trip trip1;
    private Trip trip2;
    private Trip trip3;

    private LocalDate testDate;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {

        // ========================================
        // GIVEN: Crear rutas
        // ========================================

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

        // ========================================
        // GIVEN: Crear un Bus real
        // ========================================

        bus = busRepository.save(
                Bus.builder()
                        .plate("ABC123")
                        .capacity(40)
                        .available(true)
                        .amenities(List.of("WiFi", "A/C", "Toilet"))
                        .build()
        );

        testDate = LocalDate.now();
        now = LocalDateTime.now();

        // ========================================
        // GIVEN: Crear Trips
        // ========================================

        trip1 = tripRepository.save(
                Trip.builder()
                        .route(routeA)
                        .bus(bus)
                        .date(testDate)
                        .departureAt(now.plusHours(2))
                        .arrivalEta(now.plusHours(5))
                        .status(TripStatus.SCHEDULED)
                        .build()
        );

        trip2 = tripRepository.save(
                Trip.builder()
                        .route(routeA)
                        .bus(bus)
                        .date(testDate)
                        .departureAt(now.plusHours(3))
                        .arrivalEta(now.plusHours(6))
                        .status(TripStatus.BOARDING)
                        .build()
        );

        trip3 = tripRepository.save(
                Trip.builder()
                        .route(routeB)
                        .bus(bus)
                        .date(testDate)
                        .departureAt(now.minusHours(1))
                        .arrivalEta(now.plusHours(2))
                        .status(TripStatus.DEPARTED)
                        .build()
        );
    }


    // =======================================================
    // TEST 1: findByRouteIdAndDateAndStatusIn
    // =======================================================

    @Test
    void shouldFindTripsByRouteDateAndStatuses() {
        // WHEN
        List<Trip> result = tripRepository.findByRouteIdAndDateAndStatusIn(
                routeA.getId(),
                testDate,
                List.of(TripStatus.SCHEDULED, TripStatus.BOARDING)
        );

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getRoute().getId().equals(routeA.getId())));
    }

    @Test
    void shouldReturnEmptyForNoMatchingStatus() {
        // WHEN
        List<Trip> result = tripRepository.findByRouteIdAndDateAndStatusIn(
                routeA.getId(),
                testDate,
                List.of(TripStatus.CANCELLED)
        );

        // THEN
        assertTrue(result.isEmpty());
    }


    // =======================================================
    // TEST 2: findAvailableTrips (origin/destination/date/statuses)
    // =======================================================

    @Test
    void shouldFindAvailableTripsByOriginDestinationAndDate() {
        // WHEN
        List<Trip> result = tripRepository.findAvailableTrips(
                "Bogotá",
                "Tunja",
                testDate,
                List.of(TripStatus.SCHEDULED, TripStatus.BOARDING, TripStatus.DEPARTED)
        );

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t ->
                t.getRoute().getOrigin().equals("Bogotá")
                        && t.getRoute().getDestination().equals("Tunja")
        ));
    }

    @Test
    void shouldReturnEmptyWhenOriginDestinationDoNotMatch() {
        // WHEN
        List<Trip> result = tripRepository.findAvailableTrips(
                "Medellín",
                "Cali",
                testDate,
                List.of(TripStatus.SCHEDULED)
        );

        // THEN
        assertTrue(result.isEmpty());
    }


    // =======================================================
    // TEST 3: findByStatusAndDepartureAtBefore
    // =======================================================

    @Test
    void shouldFindTripsByStatusBeforeThreshold() {
        // WHEN
        List<Trip> result = tripRepository.findByStatusAndDepartureAtBefore(
                TripStatus.DEPARTED,
                now
        );

        // THEN
        assertEquals(1, result.size());
        assertEquals(trip3.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnEmptyIfNoTripsMatchStatusBeforeThreshold() {
        // WHEN
        List<Trip> result = tripRepository.findByStatusAndDepartureAtBefore(
                TripStatus.SCHEDULED,
                now.minusHours(10)
        );

        // THEN
        assertTrue(result.isEmpty());
    }
}
