package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.FareRule;
import co.edu.unimagdalena.busreserve.domine.entities.Route;
import co.edu.unimagdalena.busreserve.domine.repositories.FareRuleRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.RouteRepository;
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

    private final FareRuleRepository fareRuleRepo;
    private final RouteRepository routeRepo;
    private final FareRuleMapper mapper;

    @Override
    public FareRuleResponse create(FareRuleCreateRequest req) {
        Route route = routeRepo.findById(req.routeId())
                .orElseThrow(() -> new NotFoundException("Route %d not found".formatted(req.routeId())));

        FareRule fareRule = mapper.toEntity(req);
        fareRule.setRoute(route);

        return mapper.toResponse(fareRuleRepo.save(fareRule));
    }

    @Override
    @Transactional(readOnly = true)
    public FareRuleResponse get(Long id) {
        return fareRuleRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("FareRule %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FareRuleResponse> listByRoute(Long routeId) {
        return fareRuleRepo.findByRouteId(routeId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FareRuleResponse findByRouteAndStops(Long routeId, Long fromStopId, Long toStopId) {
        return fareRuleRepo.findByRouteAndStops(routeId, fromStopId, toStopId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("FareRule not found for route %d and stops".formatted(routeId)));
    }

    @Override
    public FareRuleResponse update(Long id, FareRuleUpdateRequest req) {
        FareRule fareRule = fareRuleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("FareRule %d not found".formatted(id)));

        mapper.patch(fareRule, req);

        return mapper.toResponse(fareRuleRepo.save(fareRule));
    }

    @Override
    public void delete(Long id) {
        if (!fareRuleRepo.existsById(id)) {
            throw new NotFoundException("FareRule %d not found".formatted(id));
        }
        fareRuleRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculatePrice(Long routeId, Long fromStopId, Long toStopId) {
        FareRule fareRule = fareRuleRepo.findByRouteAndStops(routeId, fromStopId, toStopId)
                .orElse(null);

        if (fareRule == null) {
            return 50000.0;
        }

        Double basePrice = fareRule.getBasePrice();

        if (Boolean.TRUE.equals(fareRule.getDynamicPricing())) {
            basePrice *= 1.2;
        }

        return basePrice;
    }
}
