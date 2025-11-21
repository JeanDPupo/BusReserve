package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.api.dto.AssignmentDtos.*;
import co.edu.unimagdalena.busreserve.domine.entities.Assignment;
import co.edu.unimagdalena.busreserve.domine.entities.Trip;
import co.edu.unimagdalena.busreserve.domine.entities.User;
import co.edu.unimagdalena.busreserve.domine.repositories.AssignmentRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.TripRepository;
import co.edu.unimagdalena.busreserve.domine.repositories.UserRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.AssignmentService;
import co.edu.unimagdalena.busreserve.services.mapper.AssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepo;
    private final TripRepository tripRepo;
    private final UserRepository userRepo;
    private final AssignmentMapper mapper;

    @Override
    public AssignmentResponse create(AssignmentCreateRequest req) {

        Trip trip = tripRepo.findById(req.tripId())
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        User driver = userRepo.findById(req.driverId())
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        User dispatcher = userRepo.findById(req.dispatcherId())
                .orElseThrow(() -> new NotFoundException("Dispatcher not found"));

        validateDriverAvailability(driver.getId(), trip.getDepartureAt(), trip.getArrivalEta());
        validateBusAvailability(trip.getBus().getId(), trip.getDepartureAt(), trip.getArrivalEta());

        Assignment assignment = mapper.toEntity(req);
        assignment.setTrip(trip);
        assignment.setDriver(driver);
        assignment.setDispatcher(dispatcher);
        assignment.setAssignedAt(LocalDateTime.now());

        return mapper.toResponse(assignmentRepo.save(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse get(Long id) {
        return assignmentRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> listByTrip(Long tripId) {
        return assignmentRepo.findByTripId(tripId)
                .stream()
                .map(mapper::toResponse)
                .toList();
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
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        mapper.patch(assignment, req);

        return mapper.toResponse(assignmentRepo.save(assignment));
    }

    @Override
    public void delete(Long id) {
        Assignment assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));
        assignmentRepo.delete(assignment);
    }

    @Override
    public void approveChecklist(Long id) {
        Assignment assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        assignment.setChecklistOk(true);
        assignmentRepo.save(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateDriverAvailability(Long driverId, LocalDateTime start, LocalDateTime end) {
        List<Assignment> conflicts =
                assignmentRepo.findDriverAssignmentsOverlapping(driverId, start, end);

        if (!conflicts.isEmpty())
            throw new NotFoundException("Driver is already assigned to another trip");
    }

    @Override
    @Transactional(readOnly = true)
    public void validateBusAvailability(Long busId, LocalDateTime start, LocalDateTime end) {
        List<Assignment> conflicts =
                assignmentRepo.findBusAssignmentsOverlapping(busId, start, end);

        if (!conflicts.isEmpty())
            throw new NotFoundException("Bus is already assigned to another trip");
    }
}