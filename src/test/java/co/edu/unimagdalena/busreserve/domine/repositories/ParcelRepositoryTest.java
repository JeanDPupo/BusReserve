package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Parcel;
import co.edu.unimagdalena.busreserve.domine.entities.ParcelStatus;
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

public class ParcelRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    private Stop stopA;
    private Stop stopB;
    private Stop stopC;

    private Parcel parcel1;
    private Parcel parcel2;
    private Parcel parcel3;

    @BeforeEach
    void setUp() {

        // =========================================================
        // GIVEN: ROUTE + STOPS (según el documento del proyecto)
        // =========================================================
        Route route = routeRepository.save(
                Route.builder()
                        .code("R50")
                        .name("Ruta Central")
                        .origin("A")
                        .destination("C")
                        .distanceKm(120.0)
                        .durationMin(150)
                        .build()
        );

        stopA = stopRepository.save(
                Stop.builder()
                        .name("Stop A")
                        .lat(1.0).lng(1.0)
                        .stopOrder(1)
                        .route(route)
                        .build()
        );

        stopB = stopRepository.save(
                Stop.builder()
                        .name("Stop B")
                        .lat(2.0).lng(2.0)
                        .stopOrder(2)
                        .route(route)
                        .build()
        );

        stopC = stopRepository.save(
                Stop.builder()
                        .name("Stop C")
                        .lat(3.0).lng(3.0)
                        .stopOrder(3)
                        .route(route)
                        .build()
        );

        // =========================================================
        // GIVEN: PARCELS
        // =========================================================

        parcel1 = parcelRepository.save(
                Parcel.builder()
                        .code("PKG001")
                        .senderName("Carlos")
                        .senderPhone("3001112222")
                        .receiverName("Laura")
                        .receiverPhone("3005556666")
                        .fromStop(stopA)
                        .toStop(stopC)
                        .price(BigDecimal.valueOf(15000))
                        .status(ParcelStatus.CREATED)
                        .proofPhotoUrl(null)
                        .deliveryOtp("123456")
                        .build()
        );

        parcel2 = parcelRepository.save(
                Parcel.builder()
                        .code("PKG002")
                        .senderName("Ana")
                        .senderPhone("3002223333")
                        .receiverName("Miguel")
                        .receiverPhone("3007778888")
                        .fromStop(stopA)
                        .toStop(stopB)
                        .price(BigDecimal.valueOf(8000))
                        .status(ParcelStatus.IN_TRANSIT)
                        .proofPhotoUrl(null)
                        .deliveryOtp("654321")
                        .build()
        );

        parcel3 = parcelRepository.save(
                Parcel.builder()
                        .code("PKG003")
                        .senderName("Rosa")
                        .senderPhone("3009990000")
                        .receiverName("Lina")
                        .receiverPhone("3004445555")
                        .fromStop(stopB)
                        .toStop(stopC)
                        .price(BigDecimal.valueOf(12000))
                        .status(ParcelStatus.DELIVERED)
                        .proofPhotoUrl("https://photo.com/pic")
                        .deliveryOtp("999999")
                        .build()
        );
    }


    // =========================================================
    // TEST 1 — findByCode
    // =========================================================
    @Test
    void shouldFindParcelByCode() {

        Optional<Parcel> result = parcelRepository.findByCode("PKG001");

        assertTrue(result.isPresent());
        assertEquals(parcel1.getId(), result.get().getId());
    }


    // =========================================================
    // TEST 2 — findByCode: not found
    // =========================================================
    @Test
    void shouldReturnEmptyWhenCodeDoesNotExist() {

        Optional<Parcel> result = parcelRepository.findByCode("INVALID");

        assertTrue(result.isEmpty());
    }


    // =========================================================
    // TEST 3 — findByStatus
    // =========================================================
    @Test
    void shouldFindByStatus() {

        List<Parcel> result = parcelRepository.findByStatus(ParcelStatus.IN_TRANSIT);

        assertEquals(1, result.size());
        assertEquals(parcel2.getId(), result.get(0).getId());
    }


    // =========================================================
    // TEST 4 — findByFromStopIdAndToStopId
    // =========================================================
    @Test
    void shouldFindByFromStopIdAndToStopId() {

        List<Parcel> result =
                parcelRepository.findByFromStopIdAndToStopId(stopA.getId(), stopC.getId());

        assertEquals(1, result.size());
        assertEquals(parcel1.getId(), result.get(0).getId());
    }


    // =========================================================
    // TEST 5 — findByStatus when no parcels have that status
    // =========================================================
    @Test
    void shouldReturnEmptyWhenStatusNotFound() {

        List<Parcel> result = parcelRepository.findByStatus(ParcelStatus.FAILED);

        assertTrue(result.isEmpty());
    }
}
