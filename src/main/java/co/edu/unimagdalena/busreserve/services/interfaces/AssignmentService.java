package co.edu.unimagdalena.busreserve.services.interfaces;

import co.edu.unimagdalena.busreserve.api.dto.AssignmentDtos.*;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse create(AssignmentCreateRequest req);
    AssignmentResponse get(Long id);
    AssignmentResponse getByTrip(Long tripId);
    List<AssignmentResponse> findByDriver(Long driverId);
    List<AssignmentResponse> findByDispatcher(Long dispatcherId);
    AssignmentResponse update(Long id, AssignmentUpdateRequest req);
    void delete(Long id);
    void approveChecklist(Long id);
}
