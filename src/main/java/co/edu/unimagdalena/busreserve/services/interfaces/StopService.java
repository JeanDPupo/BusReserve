package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.StopDtos.*;

import java.util.List;

public interface StopService {
    StopResponse create(StopCreateRequest req);
    StopResponse get(Long id);
    List<StopResponse> listByRoute(Long routeId);
    StopResponse update(Long id, StopUpdateRequest req);
    void delete(Long id);
}
