package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.IncidentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.EntityType;
import co.edu.unimagdalena.busreserve.domine.entities.Incident;
import co.edu.unimagdalena.busreserve.domine.entities.IncidentType;
import co.edu.unimagdalena.busreserve.domine.repositories.IncidentRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.IncidentService;
import co.edu.unimagdalena.busreserve.services.mapper.IncidentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepo;
    private final IncidentMapper mapper;

    @Override
    public IncidentResponse create(IncidentCreateRequest req) {
        Incident incident = mapper.toEntity(req);
        return mapper.toResponse(incidentRepo.save(incident));
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponse get(Long id) {
        return incidentRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Incident %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidentResponse> list(Pageable pageable) {
        return incidentRepo.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> findByEntityTypeAndId(EntityType entityType, Long entityId) {
        return incidentRepo.findByEntityTypeAndEntityId(entityType, entityId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidentResponse> findByType(IncidentType type, Pageable pageable) {
        return incidentRepo.findByType(type, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!incidentRepo.existsById(id)) {
            throw new NotFoundException("Incident %d not found".formatted(id));
        }
        incidentRepo.deleteById(id);
    }
}
