package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Long> {
    List<Trip> findByRouteIdAndDateAndStatusIn(Long routeId, LocalDateTime date, List<TripStatus> statuses);

    @Query("""
        SELECT t FROM Trip t 
        WHERE t.route.origin = :origin 
          AND t.route.destination = :destination 
          AND t.date = :date 
          AND t.status IN :statuses
        ORDER BY t.departureAt
        """)
    List<Trip> findAvailableTrips(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("date") LocalDateTime date,
            @Param("statuses") List<TripStatus> statuses
    );

    List<Trip> findByStatusAndDepartureAtBefore(TripStatus status, LocalDateTime threshold);
}
