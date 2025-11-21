package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.IncidentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.EntityType;
import co.edu.unimagdalena.busreserve.domine.entities.IncidentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IncidentService {
    IncidentResponse create(IncidentCreateRequest req);
    IncidentResponse get(Long id);
    Page<IncidentResponse> list(Pageable pageable);
    List<IncidentResponse> findByEntityTypeAndId(EntityType entityType, Long entityId);
    Page<IncidentResponse> findByType(IncidentType type, Pageable pageable);
    void delete(Long id);
}
