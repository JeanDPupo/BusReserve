package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.entities.Stop;
import co.edu.unimagdalena.busreserve.domine.repositories.FareRuleRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.StopRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.FareRuleService;
import co.edu.unimagdalena.busreserve.services.mapper.FareRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FareRuleServiceImpl implements FareRuleService {

    private final FareRuleRepository fareRepo;
    private final RouteRepository routeRepo;
    private final StopRepository stopRepo;
    private final FareRuleMapper mapper;

    @Override
    public FareRuleResponse create(FareRuleCreateRequest req) {
        Route route = routeRepo.findById(req.routeId())
                .orElseThrow(() -> new NotFoundException("Route not found"));

        Stop from = stopRepo.findById(req.fromStopId())
                .orElseThrow(() -> new NotFoundException("FromStop not found"));

        Stop to = stopRepo.findById(req.toStopId())
                .orElseThrow(() -> new NotFoundException("ToStop not found"));

        if (!from.getRoute().getId().equals(route.getId()) || !to.getRoute().getId().equals(route.getId()))
            throw new NotFoundException("Stops must belong to the route");

        boolean exists = fareRepo.findByRouteAndStops(
                req.routeId(), req.fromStopId(), req.toStopId()
        ).isPresent();

        if (exists)
            throw new NotFoundException("FareRule already exists for this segment");

        FareRule rule = mapper.toEntity(req);
        rule.setRoute(route);
        rule.setFromStop(from);
        rule.setToStop(to);

        return mapper.toResponse(fareRepo.save(rule));
    }

    @Override
    @Transactional(readOnly = true)
    public FareRuleResponse get(Long id) {
        return fareRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("FareRule not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FareRuleResponse> listByRoute(Long routeId) {
        return fareRepo.findByRouteId(routeId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FareRuleResponse findByRouteAndStops(Long routeId, Long fromStopId, Long toStopId) {
        return fareRepo.findByRouteAndStops(routeId, fromStopId, toStopId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("FareRule not found for segment"));
    }

    @Override
    public FareRuleResponse update(Long id, FareRuleUpdateRequest req) {
        FareRule rule = fareRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("FareRule not found"));

        mapper.patch(rule, req);

        return mapper.toResponse(fareRepo.save(rule));
    }

    @Override
    public void delete(Long id) {
        if (!fareRepo.existsById(id))
            throw new NotFoundException("FareRule not found");

        fareRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculatePrice(Long routeId, Long fromStopId, Long toStopId) {
        return fareRepo.findByRouteAndStops(routeId, fromStopId, toStopId)
                .map(fr -> fr.getBasePrice().doubleValue())
                .orElseThrow(() -> new NotFoundException("FareRule not found for segment"));
    }
}