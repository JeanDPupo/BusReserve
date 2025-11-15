package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.RouteDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.RouteService;
import co.edu.unimagdalena.busreserve.services.mapper.RouteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepo;
    private final RouteMapper mapper;

    @Override
    public RouteResponse create(RouteCreateRequest req) {
        Route route = mapper.toEntity(req);
        return mapper.toResponse(routeRepo.save(route));
    }

    @Override
    @Transactional(readOnly = true)
    public RouteResponse get(Long id) {
        return routeRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Route %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouteResponse> list(Pageable pageable) {
        return routeRepo.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteResponse> findByOriginAndDestination(String origin, String destination) {
        return routeRepo.findByOriginAndDestination(origin, destination)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RouteResponse update(Long id, RouteUpdateRequest req) {
        Route route = routeRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Route %d not found".formatted(id)));

        mapper.patch(route, req);

        return mapper.toResponse(routeRepo.save(route));
    }

    @Override
    public void delete(Long id) {
        if (!routeRepo.existsById(id)) {
            throw new NotFoundException("Route %d not found".formatted(id));
        }
        routeRepo.deleteById(id);
    }
}

// ==================== StopServiceImpl.java ====================

// ==================== BusServiceImpl.java ====================

// ==================== TripServiceImpl.java ====================

// ==================== TicketServiceImpl.java ====================
