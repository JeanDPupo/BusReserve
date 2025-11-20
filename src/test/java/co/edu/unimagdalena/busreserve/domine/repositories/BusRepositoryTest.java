package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BusRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private BusRepository busRepository;

    private Bus busAvailable1;
    private Bus busAvailable2;
    private Bus busUnavailable;

    @BeforeEach
    void setUp() {

        // ==============================
        // GIVEN: Buses disponibles
        // ==============================
        busAvailable1 = busRepository.save(
                Bus.builder()
                        .plate("AAA111")
                        .capacity(40)
                        .available(true)
                        .amenities(List.of("WiFi", "USB"))
                        .build()
        );

        busAvailable2 = busRepository.save(
                Bus.builder()
                        .plate("BBB222")
                        .capacity(30)
                        .available(true)
                        .amenities(List.of("AC"))
                        .build()
        );

        // ==============================
        // GIVEN: Bus NO disponible
        // ==============================
        busUnavailable = busRepository.save(
                Bus.builder()
                        .plate("CCC333")
                        .capacity(45)
                        .available(false)
                        .amenities(List.of("USB"))
                        .build()
        );
    }


    // ============================================================
    // TEST 1: findByAvailableTrue
    // ============================================================
    @Test
    void shouldReturnOnlyAvailableBuses() {

        // ACT
        List<Bus> result = busRepository.findByAvailableTrue();

        // ASSERT
        assertEquals(2, result.size());

        assertTrue(result.contains(busAvailable1));
        assertTrue(result.contains(busAvailable2));
        assertFalse(result.contains(busUnavailable));
    }


    // ============================================================
    // TEST 2: findByPlate (bus existente)
    // ============================================================
    @Test
    void shouldFindBusByPlate() {

        // ACT
        Optional<Bus> result = busRepository.findByPlate("BBB222");

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(busAvailable2.getId(), result.get().getId());
        assertEquals("BBB222", result.get().getPlate());
    }


    // ============================================================
    // TEST 3: findByPlate (bus NO existente)
    // ============================================================
    @Test
    void shouldReturnEmptyWhenPlateNotFound() {

        // ACT
        Optional<Bus> result = busRepository.findByPlate("ZZZ999");

        // ASSERT
        assertTrue(result.isEmpty());
    }
}
