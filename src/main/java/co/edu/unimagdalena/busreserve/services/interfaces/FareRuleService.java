package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.FareRuleDtos.*;

import java.util.List;

public interface FareRuleService {
    FareRuleResponse create(FareRuleCreateRequest req);
    FareRuleResponse get(Long id);
    List<FareRuleResponse> listByRoute(Long routeId);
    FareRuleResponse findByRouteAndStops(Long routeId, Long fromStopId, Long toStopId);
    FareRuleResponse update(Long id, FareRuleUpdateRequest req);
    void delete(Long id);
    Double calculatePrice(Long routeId, Long fromStopId, Long toStopId);
}
