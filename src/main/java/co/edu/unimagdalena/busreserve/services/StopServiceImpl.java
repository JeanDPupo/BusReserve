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

import java.util.Comparator;
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
                .orElseThrow(() -> new NotFoundException("Route not found"));

        boolean sameOrderExists = stopRepo.findByRouteId(req.routeId()).stream()
                .anyMatch(s -> s.getStopOrder().equals(req.stopOrder()));
        if (sameOrderExists)
            throw new NotFoundException("Stop order already exists in route");

        Stop stop = mapper.toEntity(req);
        stop.setRoute(route);

        return mapper.toResponse(stopRepo.save(stop));
    }

    @Override
    @Transactional(readOnly = true)
    public StopResponse get(Long id) {
        return stopRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Stop not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StopResponse> listByRoute(Long routeId) {
        return stopRepo.findByRouteId(routeId)
                .stream()
                .sorted(Comparator.comparing(Stop::getStopOrder))
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public StopResponse update(Long id, StopUpdateRequest req) {
        Stop stop = stopRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Stop not found"));

        mapper.patch(stop, req);

        return mapper.toResponse(stopRepo.save(stop));
    }

    @Override
    public void delete(Long id) {
        Stop stop = stopRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Stop not found"));

        stopRepo.delete(stop);
    }
}