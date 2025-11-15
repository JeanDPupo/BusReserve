package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.RouteDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RouteService {
    RouteResponse create(RouteCreateRequest req);
    RouteResponse get(Long id);
    Page<RouteResponse> list(Pageable pageable);
    List<RouteResponse> findByOriginAndDestination(String origin, String destination);
    RouteResponse update(Long id, RouteUpdateRequest req);
    void delete(Long id);
}
