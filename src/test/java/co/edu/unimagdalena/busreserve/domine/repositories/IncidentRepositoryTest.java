package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidentRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private BusRepository busRepository;

    private Trip trip;

    private Incident incidentTrip1;
    private Incident incidentTrip2;
    private Incident incidentTicket1;

    @BeforeEach
    void setUp() {

        // ============================
        // GIVEN: Route
        // ============================
        Route route = routeRepository.save(
                Route.builder()
                        .code("R10")
                        .name("Ruta Principal")
                        .origin("A")
                        .destination("B")
                        .distanceKm(100.0)
                        .durationMin(120)
                        .build()
        );

        stopRepository.save(Stop.builder().route(route).name("Stop1").stopOrder(1).lat(1.).lng(1.).build());
        stopRepository.save(Stop.builder().route(route).name("Stop2").stopOrder(2).lat(2.).lng(2.).build());

        // ============================
        // GIVEN: Bus
        // ============================
        Bus bus = busRepository.save(
                Bus.builder()
                        .plate("AAA111")
                        .capacity(40)
                        .available(true)
                        .amenities(List.of())
                        .build()
        );

        // ============================
        // GIVEN: Trip
        // ============================
        trip = tripRepository.save(
                Trip.builder()
                        .route(route)
                        .bus(bus)
                        .date(java.time.LocalDate.now())
                        .departureAt(LocalDateTime.now().plusHours(1))
                        .arrivalEta(LocalDateTime.now().plusHours(3))
                        .status(TripStatus.SCHEDULED)
                        .build()
        );

        // ============================
        // GIVEN: Incidents
        // ============================

        // Incidente 1 sobre TRIP
        incidentTrip1 = incidentRepository.save(
                Incident.builder()
                        .entityType(EntityType.TRIP)
                        .entityId(trip.getId())
                        .type(IncidentType.SECURITY)
                        .note("Amenaza leve en el bus")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // Incidente 2 sobre TRIP
        incidentTrip2 = incidentRepository.save(
                Incident.builder()
                        .entityType(EntityType.TRIP)
                        .entityId(trip.getId())
                        .type(IncidentType.VEHICLE)
                        .note("Luces delanteras defectuosas")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // Incidente sobre TICKET
        incidentTicket1 = incidentRepository.save(
                Incident.builder()
                        .entityType(EntityType.TICKET)
                        .entityId(200L) // ID ficticio de ticket
                        .type(IncidentType.OVERBOOK)
                        .note("Sobreventa detectada en reserva")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }


    // ============================
    // TEST 1: findByEntityTypeAndEntityId (TRIP)
    // ============================
    @Test
    void shouldFindIncidentsForTrip() {

        List<Incident> incidents =
                incidentRepository.findByEntityTypeAndEntityId(
                        EntityType.TRIP,
                        trip.getId()
                );

        assertEquals(2, incidents.size());
        assertTrue(incidents.contains(incidentTrip1));
        assertTrue(incidents.contains(incidentTrip2));
    }


    // ============================
    // TEST 2: findByEntityTypeAndEntityId (TICKET)
    // ============================
    @Test
    void shouldFindIncidentsForTicket() {

        List<Incident> incidents =
                incidentRepository.findByEntityTypeAndEntityId(
                        EntityType.TICKET,
                        200L
                );

        assertEquals(1, incidents.size());
        assertEquals(incidentTicket1.getId(), incidents.get(0).getId());
    }


    // ============================
    // TEST 3: findByType paginado
    // ============================
    @Test
    void shouldFindIncidentsByTypeWithPagination() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Incident> page =
                incidentRepository.findByType(IncidentType.VEHICLE, pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(incidentTrip2.getId(), page.getContent().get(0).getId());
    }


    // ============================
    // TEST 4: findByType vacío
    // ============================
    @Test
    void shouldReturnEmptyPageWhenNoIncidentsOfType() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Incident> page =
                incidentRepository.findByType(IncidentType.DELIVERY_FAIL, pageable);

        assertTrue(page.isEmpty());
    }
}
