package co.edu.unimagdalena.busreserve.domine.repositories;

import co.edu.unimagdalena.busreserve.domine.entities.EntityType;
import co.edu.unimagdalena.busreserve.domine.entities.Incident;
import co.edu.unimagdalena.busreserve.domine.entities.IncidentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident,Long> {
    List<Incident> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
    Page<Incident> findByType(IncidentType type, Pageable pageable);
}
