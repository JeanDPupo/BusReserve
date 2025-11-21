package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeatRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    private Bus bus;
    private Seat seat1;
    private Seat seat2;
    private Seat seat3;

    @BeforeEach
    void setUp() {

        // ===============================
        // GIVEN: Route + Stops
        // ===============================

        Route route = routeRepository.save(
                Route.builder()
                        .code("R100")
                        .name("Ruta del Norte")
                        .origin("A")
                        .destination("B")
                        .distanceKm(200.0)
                        .durationMin(260)
                        .build()
        );

        stopRepository.save(Stop.builder().route(route).name("Stop A").stopOrder(1).lat(1.).lng(1.).build());
        stopRepository.save(Stop.builder().route(route).name("Stop B").stopOrder(2).lat(2.).lng(2.).build());

        // ===============================
        // GIVEN: Bus
        // ===============================

        bus = busRepository.save(
                Bus.builder()
                        .plate("BUS-999")
                        .capacity(3)
                        .available(true)
                        .amenities(List.of("AC"))
                        .build()
        );

        // ===============================
        // GIVEN: Seats
        // ===============================

        seat1 = seatRepository.save(
                Seat.builder()
                        .bus(bus)
                        .number(1)
                        .type(SeatType.STANDARD)
                        .build()
        );

        seat2 = seatRepository.save(
                Seat.builder()
                        .bus(bus)
                        .number(2)
                        .type(SeatType.PREFERENTIAL)
                        .build()
        );

        seat3 = seatRepository.save(
                Seat.builder()
                        .bus(bus)
                        .number(3)
                        .type(SeatType.STANDARD)
                        .build()
        );
    }


    // ===============================
    // TEST 1: findByBusId
    // ===============================
    @Test
    void shouldFindAllSeatsByBusId() {

        List<Seat> seats = seatRepository.findByBusId(bus.getId());

        assertEquals(3, seats.size());
        assertTrue(seats.contains(seat1));
        assertTrue(seats.contains(seat2));
        assertTrue(seats.contains(seat3));
    }


    // ===============================
    // TEST 2: findByBusIdAndNumber
    // ===============================
    @Test
    void shouldFindSeatByBusIdAndNumber() {

        Optional<Seat> seat = seatRepository.findByBusIdAndNumber(bus.getId(), 2);

        assertTrue(seat.isPresent());
        assertEquals(seat2.getId(), seat.get().getId());
    }


    // ===============================
    // TEST 3: Seat not found
    // ===============================
    @Test
    void shouldReturnEmptyForInvalidSeatNumber() {

        Optional<Seat> seat = seatRepository.findByBusIdAndNumber(bus.getId(), 99);

        assertTrue(seat.isEmpty());
    }


    // ===============================
    // TEST 4: Empty list for wrong busId
    // ===============================
    @Test
    void shouldReturnEmptyListForWrongBus() {

        List<Seat> seats = seatRepository.findByBusId(999L);

        assertTrue(seats.isEmpty());
    }
}
