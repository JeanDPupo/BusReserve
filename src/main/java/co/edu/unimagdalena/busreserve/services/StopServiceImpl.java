package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.StopDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.StopRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.StopService;
import co.edu.unimagdalena.busreserve.services.mapper.StopMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StopServiceImpl implements StopService {

    private final StopRepository stopRepo;
    private final RouteRepository routeRepo;
    private final StopMapper mapper;

    @Override
    public StopResponse create(StopCreateRequest req) {
        Route route = routeRepo.findById(req.routeId())
                .orElseThrow(() -> new NotFoundException("Route %d not found".formatted(req.routeId())));

        Stop stop = mapper.toEntity(req);
        stop.setRoute(route);

        return mapper.toResponse(stopRepo.save(stop));
    }

    @Override
    @Transactional(readOnly = true)
    public StopResponse get(Long id) {
        return stopRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Stop %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StopResponse> listByRoute(Long routeId) {
        if (!routeRepo.existsById(routeId)) {
            throw new NotFoundException("Route %d not found".formatted(routeId));
        }
        return stopRepo.findByRouteIdOrderByOrderAsc(routeId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public StopResponse update(Long id, StopUpdateRequest req) {
        Stop stop = stopRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Stop %d not found".formatted(id)));

        mapper.patch(stop, req);

        return mapper.toResponse(stopRepo.save(stop));
    }

    @Override
    public void delete(Long id) {
        if (!stopRepo.existsById(id)) {
            throw new NotFoundException("Stop %d not found".formatted(id));
        }
        stopRepo.deleteById(id);
    }
}
