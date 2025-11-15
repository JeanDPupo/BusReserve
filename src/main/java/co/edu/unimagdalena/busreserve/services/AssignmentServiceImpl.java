package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.AssignmentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import co.edu.unimagdalena.busreserve.domine.entities.Role;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import co.edu.unimagdalena.busreserve.domine.repositories.AssigmentRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TripRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.UserRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.AssignmentService;
import co.edu.unimagdalena.busreserve.services.mapper.AssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssigmentRepository assignmentRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;
    private final AssignmentMapper mapper;

    @Override
    public AssignmentResponse create(AssignmentCreateRequest req) {
        Trip trip = tripRepo.findById(req.tripId())
                .orElseThrow(() -> new NotFoundException("Trip %d not found".formatted(req.tripId())));

        User driver = userRepo.findById(req.driverId())
                .orElseThrow(() -> new NotFoundException("Driver %d not found".formatted(req.driverId())));

        if (driver.getRole() != Role.DRIVER) {
            throw new IllegalStateException("User must be a driver");
        }

        User dispatcher = userRepo.findById(req.dispatcherId())
                .orElseThrow(() -> new NotFoundException("Dispatcher %d not found".formatted(req.dispatcherId())));

        if (dispatcher.getRole() != Role.DISPATCHER) {
            throw new IllegalStateException("User must be a dispatcher");
        }

        Assignment assignment = mapper.toEntity(req);
        assignment.setTrip(trip);
        assignment.setDriver(driver);
        assignment.setDispatcher(dispatcher);

        return mapper.toResponse(assignmentRepo.save(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse get(Long id) {
        return assignmentRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Assignment %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse getByTrip(Long tripId) {
        return assignmentRepo.findByTripId(tripId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Assignment for trip %d not found".formatted(tripId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> findByDriver(Long driverId) {
        return assignmentRepo.findByDriverId(driverId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> findByDispatcher(Long dispatcherId) {
        return assignmentRepo.findByDispatcherId(dispatcherId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public AssignmentResponse update(Long id, AssignmentUpdateRequest req) {
        Assignment assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment %d not found".formatted(id)));

        mapper.patch(assignment, req);

        return mapper.toResponse(assignmentRepo.save(assignment));
    }

    @Override
    public void delete(Long id) {
        if (!assignmentRepo.existsById(id)) {
            throw new NotFoundException("Assignment %d not found".formatted(id));
        }
        assignmentRepo.deleteById(id);
    }

    @Override
    public void approveChecklist(Long id) {
        Assignment assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment %d not found".formatted(id)));

        assignment.setChecklistOk(true);
        assignmentRepo.save(assignment);
    }
}
