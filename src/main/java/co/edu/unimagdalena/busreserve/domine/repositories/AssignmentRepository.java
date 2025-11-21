package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

    // ===============================================================
    // 1. Obtener asignaciones por viaje
    // ===============================================================
    List<Assignment> findByTripId(Long tripId);

    // ===============================================================
    // 2. Obtener asignaciones por conductor
    // ===============================================================
    List<Assignment> findByDriverId(Long driverId);

    // ===============================================================
    // 3. Obtener asignaciones por despachador
    // ===============================================================
    List<Assignment> findByDispatcherId(Long dispatcherId);

    // ===============================================================
    // 4. Obtener asignaciones por bus
    //    NECESARIO según el PDF (evitar doble asignación del bus)
    // ===============================================================
    List<Assignment> findByTrip_Bus_Id(Long busId);

    // ===============================================================
    // 5. Validar traslape de asignaciones para conductores
    //
    //    Regla del proyecto:
    //    “Un conductor no puede estar asignado en dos viajes
    //     simultáneos”.
    //
    //    Overlap rule:
    //       A.start < B.end
    //       A.end   > B.start
    // ===============================================================
    @Query("""
        SELECT a FROM Assignment a 
        WHERE a.driver.id = :driverId
          AND a.trip.departureAt < :endTime
          AND a.trip.arrivalEta > :startTime
    """)
    List<Assignment> findDriverAssignmentsOverlapping(
            @Param("driverId") Long driverId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );


    // ===============================================================
    // 6. Validar traslape de asignaciones para buses
    //
    //    Regla del proyecto:
    //    “Un bus no puede estar en dos viajes simultáneos”
    // ===============================================================
    @Query("""
        SELECT a FROM Assignment a 
        WHERE a.trip.bus.id = :busId
          AND a.trip.departureAt < :endTime
          AND a.trip.arrivalEta > :startTime
    """)
    List<Assignment> findBusAssignmentsOverlapping(
            @Param("busId") Long busId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
