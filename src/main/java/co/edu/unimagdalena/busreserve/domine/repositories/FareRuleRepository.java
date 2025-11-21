package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FareRuleRepository extends JpaRepository<FareRule,Long> {
    List<FareRule> findByRouteId(Long routeId);

    @Query("""
        SELECT fr FROM FareRule fr 
        WHERE fr.route.id = :routeId 
          AND fr.fromStop.id = :fromStopId 
          AND fr.toStop.id = :toStopId
        """)
    Optional<FareRule> findByRouteAndStops(
            @Param("routeId") Long routeId,
            @Param("fromStopId") Long fromStopId,
            @Param("toStopId") Long toStopId
    );
}
